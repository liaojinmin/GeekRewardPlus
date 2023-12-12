package me.neon.reward.menu


/**
 * 作者: 老廖
 * 时间: 2022/7/5
 */
class MenuData(

    val title: String,

    /**
     * 菜单字符布局
     */
    val layout: Array<String>,

    var menuIcon: MutableMap<Char, MenuIcon>
) {

    val itemUISlots by lazy {
        mutableListOf<Int>().apply {
            var index = 0
            layout.forEach { d ->
                d.toCharArray().forEach { c ->
                    if (c == '@') add(index)
                    index++
                }
            }
        }
    }


}