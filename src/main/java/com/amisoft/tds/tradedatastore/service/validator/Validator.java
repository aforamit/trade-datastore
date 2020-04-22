package com.amisoft.tds.tradedatastore.service.validator;

import java.util.List;

public interface Validator<T> {
    void validate(T latest, List<String> validationErrorMessages);
}
