package com.mastersessay.blockchain.accounting.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.SORT_TYPE_VALUE_ASC;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.SORT_TYPE_VALUE_DESC;

@Component
public class PageUtils {
    public PageRequest formPageRequest(Integer start,
                                       Integer count,
                                       String sortBy,
                                       String sortType) {
        Sort sort = Sort.by(sortBy).ascending();

        if (SORT_TYPE_VALUE_ASC.equals(sortType)) {
            sort = Sort.by(sortBy).ascending();
        } else if (SORT_TYPE_VALUE_DESC.equals(sortType)) {
            sort = Sort.by(sortBy).descending();
        }

        return PageRequest.of(start, count, sort);
    }
}
