package com.sasaj.graphics.drawingapp.main

import com.sasaj.graphics.drawingapp.entities.DrawingUI

data class DrawingsListNavigationViewState ( var imagePath: String? = null,
                                             var list: List<DrawingUI>? = null)