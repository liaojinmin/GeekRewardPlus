package me.geek.reward.menu


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
            menuIcon.forEach { (_, value) ->
                if (value.packID != null) {
                    add(index)
                }
                index++
            }
        }
    }


}