package tech.iooo.boot.core.utils.sorting;

import java.util.function.Function;

/**
 * Created on 2019-03-18 17:41
 * 
 * 七种排序的各种特征:
 * 其中插入排序，选择排序，冒泡排序都是简单排序，时间复杂度是O(N2),
 * 其中插入排序和冒泡排序适合原始序列有序的数组，选择排序的交换和赋值次数会比较少，可以根据不同环境和数据的
 * 实际情况和长度选择具体的排序。整体插入排序优于选择排序优于冒泡排序。
 * 
 * 希尔排序是插入排序的优化，突破了前三个排序O(N2)的时间复杂度，但是本质还是插入排序，突破比较相邻元素的惯
 * 性思维，直接比较一定间隔的元素，大幅度减少了逆序调整的比较次数和交换次数，从而达到比较理想的算法复杂度，
 * 适合对中等规模数组进行排序。
 * 
 * 堆排序是利用了最大堆的特点，始终把堆顶元素（最大元素）和最后一个元素替换，再重新构造最大堆，重复执行达到
 * 排序的效果。堆结构的特性让算法的复杂度降低到NlgN级别，但是有不方便索引元素的确定，缓存命中率较低。
 * 
 * 而归并排序则是充分运用了分治原理，把大问题不断的拆分成小问题，进行排序，再合并小数组达到整体排序的目标，
 * 归并排序即高效又可靠，唯一的缺点是需要数组长度的辅助空间，在空间成本低的时候适合使用。
 * 
 * 快速排序则解决了归并排序占用空间的问题，在数组内部用很小的辅助栈，即可完成对元素的分离，再去解决分离后的
 * 更小的数组，正常情况下拥有和归并相同级别的时间复杂度，但是得注意选取好切分元素。 实际上一个复杂的序列可
 * 能用不止一种排序，例如分治和快速排序在分割到很小的序列时再进行分割反而效率不如插入排序等简单排序，可以设
 * 置一定的阈值，先用分治或者快速排序的方式分割数组，再转换成插入等简单排序完成最终的排序。
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
interface Sorting<T extends Comparable<T>> extends Function<T[], T[]> {

}
