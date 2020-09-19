package com.example.healthdiary.utils

import com.example.healthdiary.model.Recommendation
import com.example.healthdiary.ui.CodeValue

class RecommendationsUtil {

    fun getRecommendations(): List<Recommendation> {
        return listOf(
            Recommendation(
                "Примите гибидроциклофинадол при простуде",
                "Данное лекарство позвволяет сбить высокую емпературу и обеспростудить вас",
                CodeValue.HIGH_TEMP
            ),
            Recommendation(
                "Пейте больше жидкости",
                "Из-за повышенного потооделения требуется пить больше жидкости",
                CodeValue.HIGH_TEMP
            ),
            Recommendation(
                "Пейте гранатовый сок",
                "Пониженная температура может быть свидетелем низкого уровня гемоглобина. " +
                        "Длительное употребление гранатового сока позволяет повысить уровень гемоглобина " +
                        "в крови.",
                CodeValue.LOW_TEMP
            ),
            Recommendation(
                "Вероятный Гипотиреоз, рекомендуется посетить эндокринолога",
                "Пониженная температура может быть свидетелем Гипотиреоз. " +
                        "Рекомендуется посетить эндокриногола для выявления заболевания и назначения " +
                        "соответствующего лечения",
                CodeValue.LOW_TEMP
            ),
            Recommendation(
                "Пейте мочегонные средства",
                "При повышенном давлении имеет смысл принять мочегонное средство." +
                        " Также рекомендуется снизить количество употребляемых жидкостей",
                CodeValue.HIGH_PRESS
            ),
            Recommendation(
                "Постарайтесь меньше нервничать",
                "Причиной высокого давления может быть стресс. Постарайтесь меньше нервничать",
                CodeValue.HIGH_PRESS
            ),
            Recommendation(
                "Выпейте Лозартан",
                "Данное средство понижает давление",
                CodeValue.HIGH_PRESS
            ),
            Recommendation(
                "При низком давлении обратитесь к врачу",
                "При низком давлении обратитесь к врачу",
                CodeValue.LOW_PRESS
            ),
            Recommendation(
                "Исключите жирную пищу из рациона",
                "Питайтесь растительной едой (салаты и прочая зелень) чтобы снизить вес",
                CodeValue.HIGH_WEIGHT_INDEX
            ),
            Recommendation(
                "Займитесь спортом",
                "Важно сжигать полученные за день калории, чтобы они не уходили в излишек веса",
                CodeValue.HIGH_WEIGHT_INDEX
            ),
            Recommendation(
                "Обратитесь к эндокринологу",
                "Низкий вес является свидетельством проблем в гармональной системе," +
                        " рекомендуем обратиться к эндокринологу",
                CodeValue.LOW_WEIGHT_INDEX
            ),
            Recommendation(
                "С вашим здоровьем все в порядке!",
                "Советуем вести активный образ жизни, и есть здоровую пищу для поддержания формы",
                CodeValue.FINE_CODE
            ),
            Recommendation(
                "Коронавирус может проходить безсимптомно!",
                "Несмотря на хорошие показатели, вы все равно можете заразиться коронавирусом," +
                        " рекомендуем пройти тест на коронавирус!",
                CodeValue.FINE_CODE
            )
        )
    }

}