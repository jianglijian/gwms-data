package com.best.gwms.data.basinterface;

import java.util.List;

/** Created by BG236820 on 2018/1/22. */
public interface IBaseService<P, S> {
    P getPoById(Long id);

    List<P> listPoByIdList(List<Long> idList);

    P createPo(P vo);

    void deletePo(Long id);

    P updatePo(P vo);

    List<P> searchPo(S so);

    Long countResult(S so);
}
