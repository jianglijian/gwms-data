package com.best.gwms.data.basinterface;
import com.best.gwms.data.exception.bas.DaoException;
import com.best.gwms.data.exception.bas.OptLockException;
import com.best.gwms.data.exception.code.BizExceptionCode;
import com.best.gwms.data.model.bas.AbstractPo;
import com.best.gwms.data.model.bas.SearchObject;
import com.best.gwms.data.util.EasyUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import java.util.*;

import static com.best.gwms.data.util.CommonUtil.sortByIdAscend;

public interface IBasDao<P extends AbstractPo, S extends SearchObject> {

    /** ************************* 单项操作 ****************************** */
    P getPoById(Long id);

    Long createPo(P po);

    int deletePo(Long id);

    int updatePo(P po);

    Long countResult(S so);

    /** ************************ 多项操作，List *************************** */

    /**
     * 分页 MasterDaoAspectInterceptor 这个切片会对search开头的方法进行分页处理
     *
     * @param so
     * @return
     */
    List<P> searchPo(S so);

    List<P> getPoByIdList(@Param("idList") List<Long> idList);

    int deletePoList(@Param("idList") List<Long> idList);

    default void createPoList(List<P> poList) {

        if (EasyUtil.isCollectionEmpty(poList)) {
            return;
        }

        for (P po : poList) {
            try {
                Long count = createPo(po);
                if (Long.compare(count, 1) < 0) {
                    throw new DaoException(BizExceptionCode.DAO_EXCEPTION);
                }
            } catch (OptLockException optLockException) {
                throw optLockException;
            } catch (DataAccessException dataAccessException) {
                throw dataAccessException;
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }
    }

    default void updatePoList(List<P> list) {
        list = sortByIdAscend(list);
        for (P po : list) {
            int count = 0;
            count = updatePo(po);
            if (count <= 0) {
                throw new OptLockException(BizExceptionCode.OPT_LOCK_EXCEPTION);
            }
        }
    }

    /** ************************ 多项操作，Set *************************** */
    default List<P> getPoByIdSet(Set<Long> idSet) {

        List<P> list = new ArrayList<>();

        if (EasyUtil.isCollectionEmpty(idSet)) {
            return list;
        }

        List<Long> idList = new ArrayList<>(idSet);

        try {
            list = getPoByIdList(idList);
        } catch (OptLockException optLockException) {
            throw optLockException;
        } catch (DataAccessException dataAccessException) {
            throw dataAccessException;
        } catch (Exception e) {
            throw new DaoException(e);
        }

        return list;
    }

    default void createPoSet(Set<P> poSet) {

        try {
            createPoList(new ArrayList<>(poSet));
        } catch (OptLockException optLockException) {
            throw optLockException;
        } catch (DataAccessException dataAccessException) {
            throw dataAccessException;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    default void updatePoSet(Set<P> poSet) {
        try {
            updatePoList(new ArrayList<>(poSet));
        } catch (OptLockException optLockException) {
            throw optLockException;
        } catch (DataAccessException dataAccessException) {
            throw dataAccessException;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    default void deletePoSet(Set<Long> idSet) {

        if (EasyUtil.isCollectionEmpty(idSet)) {
            return;
        }

        try {
            deletePoList(new ArrayList<>(idSet));
        } catch (OptLockException optLockException) {
            throw optLockException;
        } catch (DataAccessException dataAccessException) {
            throw dataAccessException;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /** ************************ 多项操作，Map *************************** */
    default Map<Long, P> getPoMapByIdList(List<Long> idList) {

        Map<Long, P> map = new HashMap<>();

        if (EasyUtil.isCollectionEmpty(idList)) {
            return map;
        }

        try {

            List<P> poList = getPoByIdList(idList);
            poList.forEach(po -> map.put(po.getId(), po));

        } catch (OptLockException optLockException) {
            throw optLockException;
        } catch (DataAccessException dataAccessException) {
            throw dataAccessException;
        } catch (Exception e) {
            throw new DaoException(e);
        }

        return map;
    }

    default Map<Long, P> getPoMapByIdSet(Set<Long> idSet) {

        Map<Long, P> map = new HashMap<>();

        if (EasyUtil.isCollectionEmpty(idSet)) {
            return map;
        }

        try {
            map = getPoMapByIdList(new ArrayList<>(idSet));
        } catch (OptLockException optLockException) {
            throw optLockException;
        } catch (DataAccessException dataAccessException) {
            throw dataAccessException;
        } catch (Exception e) {
            throw new DaoException(e);
        }

        return map;
    }
}
