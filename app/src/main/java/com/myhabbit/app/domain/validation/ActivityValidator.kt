package com.myhabbit.app.domain.validation

/**
 * Validator input activity sesuai PRD.md §14 Create Validation.
 *
 * Title: wajib, 1-100 karakter.
 * Description: opsional, maksimal 500 karakter.
 * Category: wajib dipilih.
 */
object ActivityValidator {

    const val TITLE_MAX_LENGTH = 100
    const val DESCRIPTION_MAX_LENGTH = 500

    fun validateTitle(title: String): String? {
        if (title.isBlank()) return "Title tidak boleh kosong"
        if (title.length > TITLE_MAX_LENGTH) return "Title maksimal $TITLE_MAX_LENGTH karakter"
        return null
    }

    fun validateDescription(description: String?): String? {
        if (description != null && description.length > DESCRIPTION_MAX_LENGTH) {
            return "Deskripsi maksimal $DESCRIPTION_MAX_LENGTH karakter"
        }
        return null
    }
}
