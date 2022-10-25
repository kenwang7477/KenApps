package com.example.tvprogramlist.repository

import com.kenwang.kenapps.data.model.TvProgram
import com.kenwang.kenapps.data.repository.tvprogramlist.TvProgramDataSource

class TvProgramRepository(private val tvProgramDataSource: TvProgramDataSource) {

    fun getProgramList(eProgram: TvProgramEnum): List<TvProgram> {
        return when (eProgram) {
            TvProgramEnum.TvProgramList1 -> tvProgramDataSource.getTvProgramList1()
            TvProgramEnum.TvProgramList2 -> tvProgramDataSource.getTvProgramList2()
        }
    }

    enum class TvProgramEnum(val title: String) {
        TvProgramList1("TVBox"),
        TvProgramList2("官網");

        companion object {

            fun getTvProgramEnum(title: String): TvProgramEnum {
                return TvProgramEnum.values().find { it.title == title } ?: TvProgramList1
            }
        }
    }
}
