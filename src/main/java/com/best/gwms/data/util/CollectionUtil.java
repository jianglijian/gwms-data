package com.best.gwms.data.util;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.util.*;

/** Created by bl03846 on 2014/10/27. */
public class CollectionUtil {
    private static final Log logger = LogFactory.getLog(CollectionUtil.class);

    private CollectionUtil() {
    }

    /**
     * 通过反射获取成员变量对应的value
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = getField(obj.getClass(), fieldName); // get the field in this object
        if (field != null) {
            field.setAccessible(true);
            try {
                return field.get(obj);
            } catch (Exception e) {
                logger.info(e.getMessage());
                return null;
            }
        }
        return null;
    }



    /**
     * 获取成员对象
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            return getField(superclass, fieldName);
        }
        return null;
    }

    /**
     * 把list转换为一个map, key 为对象T的一个属性,value为T ,返回的map是一个单值map, key不能重复,value可以重复 若key为空则返回以id为key的map
     * 若key不是对象T的成员,则返回一个空map.
     *
     * <p>该方法调用ImmutableList.copyOf(List)方法。
     *
     * @param list
     * @param key map key
     * @param <T>
     * @return
     */
    public static <P, T> Map<P, T> getSingleMap(List<T> list, String key) {
        if (EasyUtil.isCollectionEmpty(list)) {
            return Maps.newHashMap();
        }
        return getSingleMap(ImmutableList.copyOf(list), key);
    }

    /**
     * 把list转换为一个map, key 为对象T的一个属性,value为T ,返回的map是一个单值map, key不能重复,value可以重复 若key为空则返回以id为key的map
     * 若key不是对象T的成员,则返回一个空map
     *
     * @param list 只读集合
     * @param key map key
     * @param <T>
     * @return
     */
    public static <P, T> Map<P, T> getSingleMap(ImmutableList<T> list, String key) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        if (Strings.isNullOrEmpty(key)) {
            try {
                return getMapByKey(list, "id");
            } catch (NoSuchFieldException e) {
                logger.error("方法getMap在创建Map时指定的key不是对象成员");
            }
        }
        try {
            return getMapByKey(list, key);
        } catch (NoSuchFieldException e) {
            return new HashMap<>();
        }
    }

    /**
     * 把list转换为一个map,此map的 key 为对象T的一个属性{@Field key},value为Set<T> ,返回的map是一个多值map, key不能重复,
     * 若key为空则返回以id为key的map 若key不是对象T的成员,则返回一个空map
     *
     * @param list
     * @param key map key
     * @param <T>
     * @return
     */
    public static <P, T> HashMultimap<P, T> getSetMultityValueMap(List<T> list, String key) {
        if (CollectionUtils.isEmpty(list)) {
            return HashMultimap.create();
        }
        return getSetMultityValueMap(ImmutableList.copyOf(list), key);
    }

    /**
     * 把list转换为一个map,此map的 key 为对象T的一个属性{@Field key},value为Set<T> ,返回的map是一个多值map, key不能重复,
     * 若key为空则返回以id为key的map 若key不是对象T的成员,则返回一个空map
     *
     * @param list
     * @param key map key
     * @param <T>
     * @return
     */
    public static <P, T> HashMultimap<P, T> getSetMultityValueMap(ImmutableList<T> list, String key) {
        if (CollectionUtils.isEmpty(list)) {
            return HashMultimap.create();
        }
        if (Strings.isNullOrEmpty(key)) {
            try {
                return getSetMultityValueMapByKey(list, "id");
            } catch (NoSuchFieldException e) {
                logger.error("方法getMap在创建Map时指定的key不是对象成员");
            }
        }
        try {
            return getSetMultityValueMapByKey(list, key);
        } catch (NoSuchFieldException e) {
            return HashMultimap.create();
        }
    }

    @SuppressWarnings("unchecked")
    private static <P, T> HashMultimap<P, T> getSetMultityValueMapByKey(List<T> list, String key) throws NoSuchFieldException {
        if (Strings.isNullOrEmpty(key)) {
            return HashMultimap.create();
        }

        HashMultimap<P, T> map = HashMultimap.create();
        for (T t : list) {
            Field field = getField(t.getClass(), key);
            if (field == null) {
                throw new NoSuchFieldException("指定参数不是对象成员");
            }
            Object value = getFieldValue(t, key);
            if (value != null) {
                P p = (P) value;
                map.put(p, t);
            }
        }
        return map;
    }

    /**
     * 把list转换为一个map,此map的 key 为对象T的一个属性
     *
     * @param key ,value为List<T> ,返回的map是一个多值map, key不能重复, 若key为空则返回以id为key的map 若key不是对象T的成员,则返回一个空map
     * @param list
     * @param key map key
     * @param <T>
     * @return
     */
    public static <P, T> ListMultimap<P, T> getListMultityValueMap(List<T> list, String key) {
        if (CollectionUtils.isEmpty(list)) {
            return ArrayListMultimap.create();
        }
        return getListMultityValueMap(ImmutableList.copyOf(list), key);
    }

    /**
     * 把list转换为一个map,此map的 key 为对象T的一个属性
     *
     * @param key ,value为List<T> ,返回的map是一个多值map, key不能重复, 若key为空则返回以id为key的map 若key不是对象T的成员,则返回一个空map
     * @param list
     * @param key map key
     * @return
     */
    public static <P, T> ListMultimap<P, T> getListMultityValueMap(ImmutableList<T> list, String key) {
        if (CollectionUtils.isEmpty(list)) {
            return ArrayListMultimap.create();
        }
        if (Strings.isNullOrEmpty(key)) {
            try {
                return getListMultityValueMapByKey(list, "id");
            } catch (NoSuchFieldException e) {
                logger.error("方法getMap在创建Map时指定的key不是对象成员");
            }
        }
        try {
            return getListMultityValueMapByKey(list, key);
        } catch (NoSuchFieldException e) {
            return ArrayListMultimap.create();
        }
    }

    @SuppressWarnings("unchecked")
    private static <P, T> ListMultimap<P, T> getListMultityValueMapByKey(List<T> list, String key) throws NoSuchFieldException {
        if (Strings.isNullOrEmpty(key)) {
            return ArrayListMultimap.create();
        }

        ListMultimap<P, T> map = ArrayListMultimap.create();
        for (T t : list) {
            Field field = getField(t.getClass(), key);
            if (field == null) {
                throw new NoSuchFieldException("指定参数不是对象成员");
            }
            Object value = getFieldValue(t, key);
            if (value != null) {
                P p = (P) value;
                map.put(p, t);
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static <P, T> Map<P, T> getMapByKey(List<T> list, String key) throws NoSuchFieldException {
        if (Strings.isNullOrEmpty(key)) {
            return new HashMap<>();
        }

        Map<P, T> map = new HashMap<>();
        for (T t : list) {
            Field field = getField(t.getClass(), key);
            if (field == null) {
                throw new NoSuchFieldException("指定参数不是对象成员");
            }
            Object value = getFieldValue(t, key);
            if (value != null) {
                P p = (P) value;
                map.put(p, t);
            }
        }
        return map;
    }

    /**
     * 从集合元素T中获取属性fieldName的值并放在List集合中 若fieldName为""或者null,或者fieldName不是对象T的成员则返回空集合
     *
     * @param collection
     * @param fieldName
     * @param <T>
     * @param <P>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, P> List<P> getPropertyListFromCollection(Collection<T> collection, String fieldName) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>();
        }
        return getPropertyListFromCollection(ImmutableList.copyOf(collection), fieldName);
    }

    /**
     * 从集合元素T中获取属性fieldName的值并放在List集合中 若fieldName为""或者null,或者fieldName不是对象T的成员则返回空集合
     *
     * @param collection
     * @param fieldName
     * @param <T>
     * @param <P>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, P> List<P> getPropertyListFromCollection(ImmutableCollection<T> collection, String fieldName) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>();
        }
        if (Strings.isNullOrEmpty(fieldName)) {
            return new ArrayList<>();
        }

        List<P> reslut = new ArrayList<>();
        for (T t : collection) {
            Object value = getFieldValue(t, fieldName);
            if (value == null) {
                continue;
            }
            reslut.add((P) value);
        }

        return reslut;
    }

    /**
     * 从集合元素T中获取属性fieldName的值并放在List集合中 若fieldName为""或者null,或者fieldName不是对象T的成员则返回空集合
     *
     * @param list
     * @param fieldName
     * @param <T>
     * @param <P>
     * @return 不包含null元素。
     */
    @SuppressWarnings("unchecked")
    public static <T, P> List<P> getPropertyListFromList(List<T> list, String fieldName) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return getPropertyListFromList(ImmutableList.copyOf(list), fieldName);
    }

    /**
     * 从集合元素T中获取属性fieldName的值并放在List集合中 若fieldName为""或者null,或者fieldName不是对象T的成员则返回空集合
     *
     * @param list
     * @param fieldName
     * @param <T>
     * @param <P>
     * @return 不包含null元素。
     */
    @SuppressWarnings("unchecked")
    public static <T, P> List<P> getPropertyListFromList(ImmutableList<T> list, String fieldName) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        if (Strings.isNullOrEmpty(fieldName)) {
            return new ArrayList<>();
        }

        List<P> reslut = new ArrayList<>();
        for (T t : list) {
            Object value = getFieldValue(t, fieldName);
            if (value == null) {
                continue;
            }
            reslut.add((P) value);
        }

        return reslut;
    }

    /**
     * 从集合元素T中获取属性fieldName的值并放在set集合中 若fieldName为""或者null,或者fieldName不是对象T的成员则返回空集合
     *
     * @param list
     * @param fieldName
     * @param <T>
     * @param <P>
     * @return 不包含null元素。
     */
    @SuppressWarnings("unchecked")
    public static <T, P> Set<P> getPropertySetFromList(List<T> list, String fieldName) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashSet<>();
        }
        return getPropertySetFromList(ImmutableList.copyOf(list), fieldName);
    }

    /**
     * 从集合元素T中获取属性fieldName的值并放在set集合中 若fieldName为""或者null,或者fieldName不是对象T的成员则返回空集合
     *
     * @param list
     * @param fieldName
     * @param <T>
     * @param <P>
     * @return 不包含null元素。
     */
    @SuppressWarnings("unchecked")
    public static <T, P> Set<P> getPropertySetFromList(ImmutableList<T> list, String fieldName) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashSet<>();
        }
        if (Strings.isNullOrEmpty(fieldName)) {
            return new HashSet<>();
        }

        Set<P> reslut = new HashSet<>();
        for (T t : list) {
            Object value = getFieldValue(t, fieldName);
            if (value == null) {
                continue;
            }
            reslut.add((P) value);
        }

        return reslut;
    }

    /**
     * 获取多值map映射 此方法返回一个多值map,@param fieldName1 对应的值作为key,@param
     * fieldName2对应的值作为value,其中fieldName1,fieldName2都是对象T的成员
     * 若fieldName1,fieldName2不是对象T的成员,或者fieldName1,fieldName2为null或"",则返回一个空的map
     *
     * @param list
     * @param fieldName1 对象T的成员,其值作为map的key
     * @param fieldName2 对象T的成员,其值作为map的value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, K, V> HashMultimap<K, V> getHashMultiMapping(List<T> list, String fieldName1, String fieldName2) {
        HashMultimap<K, V> reslut = HashMultimap.create();
        if (CollectionUtils.isEmpty(list) || Strings.isNullOrEmpty(fieldName1) || Strings.isNullOrEmpty(fieldName2)) {
            return reslut;
        }

        return getHashMultiMapping(ImmutableList.copyOf(list), fieldName1, fieldName2);
    }

    /**
     * 获取多值map映射 此方法返回一个多值map,@param fieldName1 对应的值作为key,@param
     * fieldName2对应的值作为value,其中fieldName1,fieldName2都是对象T的成员
     * 若fieldName1,fieldName2不是对象T的成员,或者fieldName1,fieldName2为null或"",则返回一个空的map
     *
     * @param list
     * @param fieldName1 对象T的成员,其值作为map的key
     * @param fieldName2 对象T的成员,其值作为map的value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, K, V> HashMultimap<K, V> getHashMultiMapping(ImmutableList<T> list, String fieldName1, String fieldName2) {
        HashMultimap<K, V> reslut = HashMultimap.create();
        if (CollectionUtils.isEmpty(list) || Strings.isNullOrEmpty(fieldName1) || Strings.isNullOrEmpty(fieldName2)) {
            return reslut;
        }

        for (T t : list) {
            Object value1 = getFieldValue(t, fieldName1);
            Object value2 = getFieldValue(t, fieldName2);
            if (value1 == null || value2 == null) {
                continue;
            }
            reslut.put((K) value1, (V) value2);
        }
        return reslut;
    }

    /**
     * 获取多值map映射 此方法返回一个多值map,@param fieldName1 对应的值作为key,@param
     * fieldName2对应的值作为value,其中fieldName1,fieldName2都是对象T的成员
     * 若fieldName1,fieldName2不是对象T的成员,或者fieldName1,fieldName2为null或"",则返回一个空的map
     *
     * @param list
     * @param fieldName1 对象T的成员,其值作为map的key
     * @param fieldName2 对象T的成员,其值作为map的value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, K, V> ListMultimap<K, V> getListMultiMapping(List<T> list, String fieldName1, String fieldName2) {
        ListMultimap<K, V> reslut = ArrayListMultimap.create();
        if (CollectionUtils.isEmpty(list) || Strings.isNullOrEmpty(fieldName1) || Strings.isNullOrEmpty(fieldName2)) {
            return reslut;
        }

        return getListMultiMapping(ImmutableList.copyOf(list), fieldName1, fieldName2);
    }

    /**
     * 获取多值map映射 此方法返回一个多值map,@param fieldName1 对应的值作为key,@param
     * fieldName2对应的值作为value,其中fieldName1,fieldName2都是对象T的成员
     * 若fieldName1,fieldName2不是对象T的成员,或者fieldName1,fieldName2为null或"",则返回一个空的map
     *
     * @param list
     * @param fieldName1 对象T的成员,其值作为map的key
     * @param fieldName2 对象T的成员,其值作为map的value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, K, V> ListMultimap<K, V> getListMultiMapping(ImmutableList<T> list, String fieldName1, String fieldName2) {
        ListMultimap<K, V> reslut = ArrayListMultimap.create();
        if (CollectionUtils.isEmpty(list) || Strings.isNullOrEmpty(fieldName1) || Strings.isNullOrEmpty(fieldName2)) {
            return reslut;
        }

        for (T t : list) {
            Object value1 = getFieldValue(t, fieldName1);
            Object value2 = getFieldValue(t, fieldName2);
            if (value1 == null || value2 == null) {
                continue;
            }
            reslut.put((K) value1, (V) value2);
        }
        return reslut;
    }

    /**
     * 获取单值map映射 此方法返回一个单值map,@param fieldName1 对应的值作为key,@param
     * fieldName2对应的值作为value,其中fieldName1,fieldName2都是对象T的成员
     * 若fieldName1,fieldName2不是对象T的成员,或者fieldName1,fieldName2为null或"",则返回一个空的map
     *
     * @param list
     * @param key 对象T的成员,其值作为map的key
     * @param value 对象T的成员,其值作为map的value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, K, V> Map<K, V> getSingleMapping(Collection<T> list, String key, String value) {
        HashMap<K, V> reslut = Maps.newHashMap();
        if (CollectionUtils.isEmpty(list) || Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(value)) {
            return reslut;
        }

        return getSingleMapping(ImmutableList.copyOf(list), key, value);
    }

    /**
     * 获取单值map映射 此方法返回一个单值map,@param fieldName1 对应的值作为key,@param
     * fieldName2对应的值作为value,其中fieldName1,fieldName2都是对象T的成员
     * 若fieldName1,fieldName2不是对象T的成员,或者fieldName1,fieldName2为null或"",则返回一个空的map
     *
     * @param list
     * @param fieldName1 对象T的成员,其值作为map的key
     * @param fieldName2 对象T的成员,其值作为map的value
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, K, V> Map<K, V> getSingleMapping(ImmutableList<T> list, String fieldName1, String fieldName2) {
        HashMap<K, V> reslut = Maps.newHashMap();
        if (CollectionUtils.isEmpty(list) || Strings.isNullOrEmpty(fieldName1) || Strings.isNullOrEmpty(fieldName2)) {
            return reslut;
        }

        for (T t : list) {
            Object value1 = getFieldValue(t, fieldName1);
            Object value2 = getFieldValue(t, fieldName2);
            if (value1 == null || value2 == null) {
                continue;
            }
            reslut.put((K) value1, (V) value2);
        }
        return reslut;
    }

    /**
     * 获取两个集合的差集
     *
     * @param allList 所有 集合
     * @param partList 已存在的sku id 集合
     * @return
     */
    public static List<Long> getDiffList(List<Long> allList, List<Long> partList) {
        Sets.SetView<Long> setView = Sets.difference(Sets.newHashSet(allList), Sets.newHashSet(partList));
        Set<Long> sets = Sets.newHashSet();
        if (EasyUtil.isCollectionNotEmpty(setView)) {
            setView.copyInto(sets);
        }
        return new ArrayList<>(sets);
    }

    /**
     * 改变当前的list，新list包含的对象从fromIndex开始，不包含endindex
     *
     * @param list
     * @param fromIndex
     * @param endIndex
     */
    public static <T> void modifyList(List<T> list, int fromIndex, int endIndex) {
        if (EasyUtil.isCollectionEmpty(list) || list.size() <= (endIndex - fromIndex)) {
            return;
        }
        List<T> temp = new ArrayList<T>();
        for (int i = fromIndex; i < endIndex; i++) {
            temp.add(list.get(i));
        }
        list.clear();
        list.addAll(temp);
    }

    /**
     * 移除集合中为null的对象
     *
     * @param c
     */
    public static <T> void removeNullFromCollection(Collection<T> c) {
        if (EasyUtil.isCollectionNotEmpty(c)) {
            Iterator<T> it = c.iterator();
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 移除集合中为null的对象.
     *
     * @author bl05386
     * @param l
     */
    public static <T> void removeNullFromCollection(List<T> l) {
        if (EasyUtil.isCollectionNotEmpty(l)) {
            Iterator<T> it = l.iterator();
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                }
            }
        }
    }

    /**
     * Returns the elements of {@code unfiltered} that satisfy a predicate. The returned collection is
     * a live view of {@code unfiltered}; changes to one affect the other.
     *
     * <p>The resulting collection's iterator does not support {@code remove()}, but all other
     * collection methods are supported. When given an element that doesn't satisfy the predicate, the
     * collection's {@code add()} and {@code addAll()} methods throw an {@link
     * IllegalArgumentException}. When methods such as {@code removeAll()} and {@code clear()} are
     * called on the filtered collection, only elements that satisfy the filter will be removed from
     * the underlying collection.
     *
     * <p>The returned collection isn't threadsafe or serializable, even if {@code unfiltered} is.
     *
     * <p>Many of the filtered collection's methods, such as {@code size()}, iterate across every
     * element in the underlying collection and determine which elements satisfy the filter. When a
     * live view is <i>not</i> needed, it may be faster to copy {@code Iterables.filter(unfiltered,
     * predicate)} and use the copy.
     *
     * <p><b>Warning:</b> {@code predicate} must be <i>consistent with equals</i>, as documented at
     * {@link Predicate#apply}. Do not provide a predicate such as {@code
     * Predicates.instanceOf(ArrayList.class)}, which is inconsistent with equals. (See {@link
     * Iterables#filter(Iterable, Class)} for related functionality.)
     */
    public static <T> List<T> filter(Collection<T> collect, Predicate<T> predicate) {
        if (EasyUtil.isCollectionEmpty(collect)) {
            return Lists.newArrayList();
        }

        Collection<T> collection = Collections2.filter(collect, predicate);
        return Lists.newArrayList(collection);
    }

    /**
     * Returns a list that applies {@code function} to each element of {@code fromList}. The returned
     * list is a transformed view of {@code fromList}; changes to {@code fromList} will be reflected
     * in the returned list and vice versa.
     *
     * <p>Since functions are not reversible, the transform is one-way and new items cannot be stored
     * in the returned list. The {@code add}, {@code addAll} and {@code set} methods are unsupported
     * in the returned list.
     *
     * <p>The function is applied lazily, invoked when needed. This is necessary for the returned list
     * to be a view, but it means that the function will be applied many times for bulk operations
     * like {@link List#contains} and {@link List#hashCode}. For this to perform well, {@code
     * function} should be fast. To avoid lazy evaluation when the returned list doesn't need to be a
     * view, copy the returned list into a new list of your choosing.
     *
     * <p>If {@code fromList} implements {@link RandomAccess}, so will the returned list. The returned
     * list is threadsafe if the supplied list and function are.
     *
     * <p>If only a {@code Collection} or {@code Iterable} input is available, use {@link
     * Collections2#transform} or {@link Iterables#transform}.
     *
     * <p><b>Note:</b> serializing the returned list is implemented by serializing {@code fromList},
     * its contents, and {@code function} -- <i>not</i> by serializing the transformed values. This
     * can lead to surprising behavior, so serializing the returned list is <b>not recommended</b>.
     * Instead, copy the list using {@link ImmutableList#copyOf(Collection)} (for example), then
     * serialize the copy. Other methods similar to this do not implement serialization at all for
     * this reason.
     */
    public static <T, F> List<F> transform2List(Collection<T> collect, Function<? super T, ? extends F> function) {
        if (EasyUtil.isCollectionEmpty(collect)) {
            return Lists.newArrayList();
        }
        List<T> list = collect instanceof List ? (List<T>) collect : Lists.newArrayList(collect);
        List<F> res = Lists.transform(list, function);

        return Lists.newArrayList(res);
    }

    /**
     * Returns a Set that applies {@code function} to each element of {@code fromCollection}. The
     * returned Set is a live view of {@code fromCollection}; changes to one affect the other.
     *
     * <p>The returned Set isn't threadsafe or serializable, even if {@code fromCollection} is.
     *
     * <p>When a live view is <i>not</i> needed, it may be faster to copy the transformed collection
     * and use the copy.
     */
    public static <T, F> Set<F> transform2Set(Collection<T> collect, Function<? super T, ? extends F> function) {
        if (EasyUtil.isCollectionEmpty(collect)) {
            return Sets.newHashSet();
        }
        Collection<? extends F> collection = Collections2.transform(collect, function);
        return Sets.newHashSet(collection);
    }

    /**
     * 泛型类型转换。
     *
     * @author bl05386
     * @param srcPoList
     * @param targetClass
     * @return
     * @throws IllegalArgumentException
     */
    public static <T, E> List<T> transfer(List<E> srcPoList, Class<T> targetClass) {

        if (targetClass == null) {
            throw new IllegalArgumentException("class1 is null");
        }

        if (EasyUtil.isCollectionEmpty(srcPoList)) {
            return Lists.newArrayList();
        }

        List<T> targetList = Lists.newArrayList();
        try {
            for (E any : srcPoList) {
                T target = targetClass.cast(any);
                targetList.add(target);
            }
        } catch (ClassCastException exception) {
            throw new IllegalArgumentException("the runtime type of E is not the subClass of the runtime type of T");
        }

        return targetList;
    }

    public static <T, E> Set<E> getPropertySetFromSet(Set<T> set, String fieldName) {
        if (EasyUtil.isCollectionEmpty(set)) {
            return new HashSet<E>();
        }

        List<T> list = Lists.newArrayList(set);
        return getPropertySetFromList(list, fieldName);
    }
}
