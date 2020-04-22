package com.amisoft.tds.tradedatastore.service.validator;

import com.amisoft.tds.tradedatastore.model.Trade;
import com.amisoft.tds.tradedatastore.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@Component
public class TradeVersionValidator implements Validator<Trade> {

    public static final String ERROR_MESSAGE_LOWER_VERSION_THAN_EXISTING =
            "Latest trade version for %s is lower than the existing one. latest/current : %s/%s";

    @Autowired
    private TradeRepository repository;

    @Override
    public void validate(Trade latest, List<String> validationErrorMessages) {
        Integer currentMaxVersion = repository.findMaxVersionById(latest.getId().getId());
        if (isLatestVersionLower(latest, currentMaxVersion)) {
            validationErrorMessages.add(errorMessage(latest, currentMaxVersion));
        }
    }

    private boolean isLatestVersionLower(Trade latest, Integer currentMaxVersion) {
        return currentMaxVersion != null && latest.getId().getVersion() < currentMaxVersion;
    }

    private String errorMessage(Trade latest, Integer currentMaxVersion) {
        return format(ERROR_MESSAGE_LOWER_VERSION_THAN_EXISTING,
                latest.getId().getId(), latest.getId().getVersion(), currentMaxVersion);
    }
}
