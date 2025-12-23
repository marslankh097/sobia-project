package com.example.budgetly.ui.banking.institution
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ui.banking.institution.content.InstitutionRow
import com.example.budgetly.ui.banking.institution.events.InstitutionEvents
import com.example.budgetly.ui.banking.institution.state.InstitutionUiState
import com.example.budgetly.utils.defaultBgColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp


@Composable
fun InstitutionScreen(
    modifier: Modifier = Modifier,
    viewModel: InstitutionViewModel = hiltViewModel(),
    onInstitutionSelected: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val handleBack = { navigateBack() }
    BackHandler { handleBack() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(defaultBgColor)
    ) {
        TopBar("Banks List") {
            handleBack()
        }
        when (state) {
            is InstitutionUiState.Idle -> {
                // Do nothing or show a placeholder
            }
            is InstitutionUiState.Loading -> {
                LoadingComponent(textId = R.string.fetching_all_banks)
            }

            is InstitutionUiState.Success -> {
                val institutions = (state as InstitutionUiState.Success).institutions
                if (institutions.isEmpty()) {
                    EmptyComponent(
                        modifier = Modifier.fillMaxSize(),
                        image = R.drawable.img_no_banks, text = R.string.no_banks_available
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(color = secondaryBgColor)
                            .padding(horizontal = 12.sdp, vertical = 12.sdp),
                        contentPadding = PaddingValues(bottom = 12.sdp),
                        verticalArrangement = Arrangement.spacedBy(10.sdp)
                    ) {
                        items(institutions) { institution ->
                            InstitutionRow(
                                modifier = Modifier.fillMaxWidth(),
                                icon = institution.logo,
                                title = institution.name.toSafeEmptyString(),
                                subTitle = institution.countries.joinToString(", ")
                            ) {
                                onInstitutionSelected(institution.id)
                            }
                        }
                    }
                }
            }

            is InstitutionUiState.Failure -> {
                FailureComponent(
                    modifier = Modifier.fillMaxSize(),
                    msg = (state as InstitutionUiState.Failure).message
                ){
                    viewModel.onEvent(InstitutionEvents.LoadInstitutions)
                }
            }
        }
    }
}

