package ksc.campus.tech.kakao.map.data.mapper

import ksc.campus.tech.kakao.map.data.entities.Document
import ksc.campus.tech.kakao.map.domain.models.SearchResult

object KakaoSearchDtoMapper {
    private fun parseCategory(category: String) =
        category.split('>').last().trim().replace(",", ", ")
    fun mapSearchResponseToSearchResult(doc:Document): SearchResult{
        return SearchResult(
            doc.id,
            doc.placeName,
            doc.addressName,
            CATEGORY_CODE_DESCRIPTION_MAP.getOrDefault(
                doc.categoryGroupCode,
                parseCategory(doc.categoryName)
            ),
            doc.y.toDoubleOrNull() ?: 0.0,
            doc.x.toDoubleOrNull() ?: 0.0
        )
    }

    private val CATEGORY_CODE_DESCRIPTION_MAP: HashMap<String, String> = hashMapOf(
        Pair("MT1", "대형마트"),
        Pair("CS2", "편의점"),
        Pair("PS3", "어린이집, 유치원"),
        Pair("SC4", "학교"),
        Pair("AC5", "학원"),
        Pair("PK6", "주차장"),
        Pair("OL7", "주유소, 충전소"),
        Pair("SW8", "지하철역"),
        Pair("BK9", "은행"),
        Pair("CT1", "문화시설"),
        Pair("AG2", "중개업소"),
        Pair("PO3", "공공기관"),
        Pair("AT4", "관광명소"),
        Pair("AD5", "숙박"),
        Pair("FD6", "음식점"),
        Pair("CE7", "카페"),
        Pair("HP8", "병원"),
        Pair("PM9", "약국")
    )
}