package com.sasaj.domain

import com.sasaj.domain.entities.Drawing
import java.io.File

interface LocalFileManager {
    fun saveFileLocallyAndReturnEntity(directory : File) : Drawing?
}