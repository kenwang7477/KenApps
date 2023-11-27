package com.example.tvprogramlist.repository

import com.kenwang.kenapps.data.model.TvProgram
import com.kenwang.kenapps.data.repository.tvprogramlist.TvProgramDataSource

class TvProgramRepository(private val tvProgramDataSource: TvProgramDataSource) {

    fun getProgramList(eProgram: TvProgramEnum): List<TvProgram> {
        return when (eProgram) {
            TvProgramEnum.TvProgramList1 -> tvProgramDataSource.getTvProgramList1()
        }
    }

    enum class TvProgramEnum(val title: String) {
        TvProgramList1("官網");

        companion object {

            fun getTvProgramEnum(title: String): TvProgramEnum {
                return entries.find { it.title == title } ?: TvProgramList1
            }
        }
    }
}
