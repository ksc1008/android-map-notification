package ksc.campus.tech.kakao.map.presentation.views.adapters

interface SearchKeywordClickCallback {
    fun clickKeyword(keyword: String)
    fun clickRemove(keyword: String)
}