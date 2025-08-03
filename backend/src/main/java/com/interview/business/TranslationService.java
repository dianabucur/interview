package com.interview.business;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final MessageSource resourceBundle;

    public String translate(String key, Object... args)
    {
        if (StringUtils.isBlank(key))
        {
            throw new IllegalArgumentException("Translation key is blank.");
        }

        return resourceBundle.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }
}
