package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankCardForUserDto;
import com.example.bankcards.dto.BankCardSearchCriteria;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.BankUser;
import com.example.bankcards.entity.enums.BankCardStatus;
import com.example.bankcards.exception.BankCardNotFoundException;
import com.example.bankcards.exception.transfer.TransferDiffOwnersException;
import com.example.bankcards.exception.transfer.TransferNegativeAmountException;
import com.example.bankcards.exception.transfer.TransferNotEnoughException;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.util.DtoConverter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class BankCardService {
    private final BankCardRepository bankCardRepository;
    private final BankUserService bankUserService;

    @Autowired
    public BankCardService(BankCardRepository bankCardRepository, BankUserService bankUserService) {
        this.bankCardRepository = bankCardRepository;
        this.bankUserService = bankUserService;
    }

    private void checkOwnership(BankCard card) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        if (!currentUserLogin.equals(card.getUser().getUsername())) {
            throw new AccessDeniedException("Access denied on card operation: you have to be card owner.");
        }

    }

    private BankCard checkPresenceAndReturn(Long cardId) {
        Optional<BankCard> bankCard = bankCardRepository.findById(cardId);
        if (bankCard.isEmpty()) {
            throw BankCardNotFoundException.byId(cardId);
        }
        return bankCard.get();
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> getAll() {
        List<BankCard> cards = bankCardRepository.findAll();
        return cards.stream()
                .map(DtoConverter::convertBankCardToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BankCardDto> getAllByUserId(Long userId) {
        List<BankCard> cards = bankCardRepository.findByUser_Id(userId);
        return cards.stream()
                .map(DtoConverter::convertBankCardToDto)
                .toList();
    }

    @Transactional
    public BankCardDto createCardByUserId(Long userId) {
        BankUser user = bankUserService.checkPresenceAndReturn(userId);

        StringBuilder gen = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            gen.append(new Random().nextInt(10));
        }

        BankCard card = new BankCard(gen.toString(),
                user,
                OffsetDateTime.now().plusMonths(12),
                BankCardStatus.ACTIVE,
                new BigDecimal(1000));
        return DtoConverter.convertBankCardToDto(bankCardRepository.save(card));
    } // ADMIN

    @Transactional
    public void deleteCard(Long cardId) {
        checkPresenceAndReturn(cardId);
        bankCardRepository.deleteById(cardId);
    } // ADMIN

    @Transactional
    public void blockCard(Long cardId) {
        BankCard card = checkPresenceAndReturn(cardId);
        card.setStatus(BankCardStatus.BLOCKED);
        card.setBlockRequested(false);
        bankCardRepository.save(card);
    } // ADMIN

    @Transactional
    public void activateCard(Long cardId) {
        BankCard card = checkPresenceAndReturn(cardId);
        card.setStatus(BankCardStatus.ACTIVE);
        bankCardRepository.save(card);
    } // ADMIN

    @Transactional
    public void sendBlockRequest(Long cardId) {
        BankCard card = checkPresenceAndReturn(cardId);
        checkOwnership(card);
        card.setBlockRequested(true);
        bankCardRepository.save(card);
    }

    @Transactional
    public void makeTransfer(Long sourceCardId, Long targetCardId, BigDecimal amount) {

        BankCard sourceCard = checkPresenceAndReturn(sourceCardId);
        BankCard targetCard = checkPresenceAndReturn(targetCardId);

        checkOwnership(sourceCard);
        checkOwnership(targetCard);

        if (!sourceCard.getUser().getId().equals(targetCard.getUser().getId())) {
            throw new TransferDiffOwnersException(sourceCardId, targetCardId, amount);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferNegativeAmountException(sourceCardId, targetCardId, amount);
        }

        if (sourceCard.getBalance().compareTo(amount) < 0) {
            throw new TransferNotEnoughException(sourceCardId, targetCardId, amount);
        }

        if (sourceCard.getStatus() != BankCardStatus.ACTIVE) {
            throw BankCardNotFoundException.byId(sourceCardId);
        }

        if (targetCard.getStatus() != BankCardStatus.ACTIVE) {
            throw BankCardNotFoundException.byId(targetCardId);
        }

        sourceCard.setBalance(sourceCard.getBalance().subtract(amount));
        targetCard.setBalance(targetCard.getBalance().add(amount));

        bankCardRepository.save(sourceCard);
        bankCardRepository.save(targetCard);
    }

    public Page<BankCardForUserDto> getUserCards(Pageable pageable, BankCardSearchCriteria searchCriteria) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        Specification<BankCard> spec = createSpecification(currentUserLogin, searchCriteria);

        Page<BankCard> cardsPage = bankCardRepository.findAll(spec, pageable);

        return cardsPage.map(BankCardForUserDto::new);
    }

    private Specification<BankCard> createSpecification(String username, BankCardSearchCriteria criteria) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.join("user").get("username"), username));

            if (criteria.balanceFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("balance"), criteria.balanceFrom()));
            }

            if (criteria.balanceTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("balance"), criteria.balanceTo()));
            }

            if (criteria.statuses() != null && !criteria.statuses().isEmpty()) {
                predicates.add(root.get("status").in(criteria.statuses()));
            }

            if (criteria.expirationDateStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("expirationDate"), criteria.expirationDateStart()));
            }

            if (criteria.expirationDateEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expirationDate"), criteria.expirationDateEnd()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
