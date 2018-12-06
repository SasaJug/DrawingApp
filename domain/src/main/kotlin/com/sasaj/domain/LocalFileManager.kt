package com.sasaj.domain

import com.sasaj.domain.entities.Drawing

interface LocalFileManager {
    fun saveFileLocallyAndReturnEntity() : Drawing?
}