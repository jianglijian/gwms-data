package com.best.gwms.data.util;
import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.model.bas.AbstractPo;
import com.best.gwms.data.model.bas.AbstractVo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**    与po相关的工具类     @author bl03846    @version 1.0.1  */
public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 调优参数：列表大小达到或者低于这插入排序将优先使用，mergesort或quicksort。
     *
     * <p>/** 将poList里面的id取出来，返回成idList，注意是无顺序的
     *
     * @param poList
     * @return
     */
    public static <P extends AbstractPo> List<Long> getIdList(Collection<P> poList) {
        Set<Long> idSet = new HashSet<Long>();
        List<Long> idList = new ArrayList<Long>();
        if (null == poList) {
            return idList;
        }
        for (AbstractPo po : poList) {
            idSet.add(po.getId());
        }
        idList.addAll(idSet);
        return idList;
    }

    public static <V extends AbstractVo> List<Long> getVoIdList(Collection<V> voList) {

        Set<Long> idSet = new HashSet<>();

        if (EasyUtil.isCollectionEmpty(voList)) {
            return new ArrayList<>();
        }

        for (AbstractVo vo : voList) {
            idSet.add(vo.getId());
        }

        return new ArrayList<>(idSet);
    }

    /**
     * 将poList里面的Id取出，返回idSet
     *
     * @param poList
     * @param <P>
     * @return
     */
    public static <P extends AbstractPo> Set<Long> getIdSet(List<P> poList) {

        Set<Long> idSet = new HashSet<>();

        if (null == poList) {
            return idSet;
        }

        for (AbstractPo po : poList) {
            idSet.add(po.getId());
        }

        return idSet;
    }

    public static <V extends AbstractVo> Set<Long> getVoIdSet(Collection<V> voList) {

        Set<Long> idSet = new HashSet<>();

        if (EasyUtil.isCollectionEmpty(voList)) {
            return new HashSet<>();
        }

        for (AbstractVo vo : voList) {
            idSet.add(vo.getId());
        }

        return idSet;
    }



    /**
     * 将poList里面的id取出来，组成map，注意id为空的会被扔掉. 若有相同的id， 则覆盖List中的前一个元素！
     *
     * @param poList
     * @return
     */
    public static <P extends AbstractPo> Map<Long, P> getMapFromPoList(List<P> poList) {
        Map<Long, P> rltMap = new HashMap<Long, P>();
        for (P po : poList) {
            if (po.getId() != null) {
                rltMap.put(po.getId(), po);
            }
        }
        return rltMap;
    }

    /**
     * 将poList里面的id取出来，组成map，注意id为空的会被扔掉. 若有相同的id， 则覆盖List中的前一个元素！
     *
     * @param voList
     * @return
     */
    public static <P extends AbstractVo> Map<Long, P> getMapFromVoList(List<P> voList) {
        Map<Long, P> rltMap = new HashMap<>();
        for (P po : voList) {
            if (po.getId() != null) {
                rltMap.put(po.getId(), po);
            }
        }
        return rltMap;
    }

    /**
     * 给指定的PoIdList按ID升序排序
     *
     * @param poIdList
     * @return
     */
    public static List<Long> sortIdList(List<Long> poIdList) {
        Collections.sort(poIdList);
        return poIdList;
    }

    // 按照PO的id冒泡升序排列
    /**
     * 此方法排序是将排序结果放进一个新对象中,原对象还是未排序状态
     *
     * @param poList {@code poList} 不等于null 且 任何的po的id属性不等于null 且 任何的po不等于null.
     * @param <P>
     * @return
     */
    public static <P extends AbstractPo> List<P> sortByIdAscend(List<P> poList) {
        if (EasyUtil.isCollectionEmpty(poList) || Integer.compare(poList.size(), 1) == 0) {
            return poList;
        }
        // 统计传入的List类型，用以排除内部类生成的List
        List<String> classNameFilterList = Lists.newArrayList("RandomAccessWrappedList");
        // 对于Arrays.asList产生的List,添加一行日志
        if (poList.getClass().isMemberClass()) {
            P p = poList.get(0);
            String className = p.getClass().getSimpleName();
            if (!classNameFilterList.contains(className)) {
                logger.info("对象 " + p.getClass().getSimpleName() + " 调用排序方法时,传入参数是通过Arrays.asList构造出来的!   ");
            }
        }

        Set<Long> idSet = CollectionUtil.getPropertySetFromList(poList, FieldConstant.ID);
        // 如果存在id重复,用冒泡排序
        if (Integer.compare(poList.size(), idSet.size()) != 0 || poList.getClass().isMemberClass()) {

            bubbleSort(poList);
        } else {
            List<Long> idList = CollectionUtil.getPropertyListFromList(poList, FieldConstant.ID);

            Collections.sort(idList, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    return Long.compare(o1, o2);
                }
            });

            Map<Long, P> poMap = CollectionUtil.getSingleMap(poList, FieldConstant.ID);

            for (int i = 0; i < idList.size(); i++) {
                P p = poMap.get(idList.get(i));
                poList.set(i, p);
            }
        }

        return poList;
    }

    /**
     * 按照po的id进行排序,并保存在传入的集合中 此方法由冒泡算法实现,仅供方法sortByIdAscend调用
     *
     * @param poList
     * @param <P>
     * @return
     */
    private static <P extends AbstractPo> List<P> bubbleSort(List<P> poList) {
        for (int i = 0; i < poList.size() - 1; i++) {
            P p = poList.get(i);
            for (int j = i + 1; j < poList.size(); j++) {
                P temp;
                P p1 = poList.get(j);
                if (Long.compare(p.getId(), p1.getId()) > 0) { // 比较两个数的大小
                    temp = p1;

                    poList.set(j, p);
                    p = temp;
                    poList.set(i, temp);
                }
            }
        }

        return poList;
    }

    /** 利用SoredSet根据集合中Id排序 */
    public static List<Long> sortListByIdSet(Set<Long> idSet) {
        if (EasyUtil.isCollectionEmpty(idSet)) {
            return new ArrayList<>();
        }
        return sortIdList(Lists.newArrayList(idSet));
    }

    /**
     * 取ID最大的返回
     *
     * @param poList
     * @param <P>
     * @return
     */
    public static <P extends AbstractPo> P getMaxIDPoOfList(List<P> poList) {
        Long id = 0L;
        P po = null;
        for (P p : poList) {
            if (p.getId().compareTo(id) > 0) {
                id = p.getId();
                po = p;
            }
        }
        return po;
    }

    /**
     * 随机指定范围内N个不重复的数 在初始化的无重复待选数组中随机产生一个数放入结果中， 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
     * 然后从len-2里随机产生下一个随机数，如此类推
     *
     * @param max 指定范围最大值
     * @param min 指定范围最小值
     * @param n 随机数个数
     * @return int[] 随机数结果集
     */
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        // 初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            // 待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            // 将随机到的数放入结果集
            result[i] = source[index];
            // 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }

    /**
     * 随机指定范围内N个不重复的数
     *
     * @param count 指定范围最大值
     * @param len 随机数个数
     * @return List<Integer> 随机数结果集
     */
    public static List<Integer> randomList(int count, int len) {
        List<Integer> resultList = new ArrayList<>();

        if (count < len) {
            return resultList;
        }

        Random rd = new Random();
        while (resultList.size() < len) {
            int nur = rd.nextInt(count); // nextInt(n)将返回一个大于等于0小于n的随机数
            if (!resultList.contains(nur)) {
                resultList.add(nur);
            }
        }

        return resultList;
    }



    /**
     * 清除AbstractPo的字段
     *
     * @param p
     * @param <P>
     */
    public static <P> void cleanAbstractPo(P p) {

        if (null == p) {
            return;
        }

        if (!(p instanceof AbstractPo)) {
            throw new IllegalArgumentException();
        }

        AbstractPo po = (AbstractPo) p;

        po.setId(null);
        po.setLockVersion(null);
        po.setCreatorId(null);
        po.setCreatedTime(null);
        po.setUpdatorId(null);
        po.setUpdatedTime(null);
    }
}
