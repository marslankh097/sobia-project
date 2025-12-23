package com.example.budgetly.domain.models.enums.filter

data class FilterOptions(val title:Int, val isSelected:Boolean = false, var filterType: FilterType)
/*
enum class FilterOptions(val title:Int, val isSelected:Boolean = false, var filterType: FilterType){

    AllMain(R.string.all,true, FilterType.Main),
    Income(R.string.income,false, FilterType.Main),
    Expense(R.string.expense,false, FilterType.Main),
    Transfer(R.string.transfer,false, FilterType.Main),

    AllFrequency(R.string.all,true, FilterType.Frequency),
    Regular(R.string.regular,false, FilterType.Frequency),
    Recurring(R.string.recurring,false, FilterType.Frequency),
    OneTime(R.string.one_time,false, FilterType.Frequency),

}*/
