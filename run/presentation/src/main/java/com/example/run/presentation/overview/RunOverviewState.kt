package com.example.run.presentation.overview

import com.example.run.presentation.overview.model.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)
