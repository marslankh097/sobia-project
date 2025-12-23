package com.example.budgetly.domain.models.api.assistant

data class ChatMessageModel(
    val isUser: Boolean,
    val content: String,
    val responseId: String? = null,
    val timestamp: Long? = null
)

/*
if I pose questions as  "question": "june expenses"....curl -X 'POST' \
'http://budgetly.trippleapps.com:8003/query' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{
"question": "june expenses"
}'
Response body
Download
{
    "original_question": "june expenses",
    "generated_sql": "SELECT\n  t.transactionId,\n  t.accountId,\n  t.categoryId,\n  t.subcategoryId,\n  t.date,\n  t.amount,\n  t.type,\n  t.frequency,\n  t.currency,\n  t.description\nFROM transactions AS t\nWHERE\n  t.type = 'expense' AND t.date BETWEEN UNIX_TIMESTAMP('2024-06-01') AND UNIX_TIMESTAMP('2024-06-30');",
    "natural_language_response": "Okay! I checked your transaction data for June, specifically looking at your expenses. It appears there were no expense transactions recorded for the month of June in the data I have.",
    "raw_results": [],
    "error": null
}
{
    "question": "may fuel expenses"
}
{
    "original_question": "may fuel expenses",
    "generated_sql": "SELECT\n  t.transactionId,\n  t.date,\n  t.amount,\n  t.currency,\n  t.description,\n  a.accountIBAN,\n  c.categoryName,\n  sc.subCategoryName\nFROM transactions AS t\nJOIN accounts AS a\n  ON t.accountId = a.accountId\nJOIN categories AS c\n  ON t.categoryId = c.categoryId\nJOIN subcategories AS sc\n  ON t.subcategoryId = sc.subCategoryId\nWHERE\n  LOWER(t.description) LIKE '%fuel%' OR LOWER(sc.subCategoryName) LIKE '%fuel%' OR LOWER(c.categoryName) LIKE '%fuel%';",
    "natural_language_response": "Okay, I found some transactions that seem related to \"fuel expenses.\" Here's a summary:\n\n*   **On Dec 3, 2024**, there's a transaction of **45.00 GBP** from account `GB29NWBK60161331926819` with the description \"Fuel for car\". It's categorized under \"Transport\" and the subcategory \"Fuel.\"\n*   **On Nov 1, 2024**, there's a transaction of **30.00 GBP** from account `GB29NWBK60161331926819` with the description \"Lunch with friends\". It's categorized under \"Transport\" and the subcategory \"Fuel.\"\n*   **On Nov 2, 2024**, there's a transaction of **45.00 GBP** from account `GB29NWBK60161331926819` with the description \"Fuel for car\". It's categorized under \"Transport\" and the subcategory \"Fuel.\"\n*   **On Nov 22, 2024**, there's a transaction of **25.00 GBP** from account `GB29NWBK60161331926819` with the description \"Cafe lunch\". It's categorized under \"Transport\" and the subcategory \"Fuel.\"",
    "raw_results": [
    [
        "793866b0-46ba-11f0-9268-a08cfddbfccc",
        1749042000000,
        "45.00",
        "GBP",
        "Fuel for car",
        "GB29NWBK60161331926819",
        "Transport",
        "Fuel"
    ],
    [
        "7939275c-46ba-11f0-9268-a08cfddbfccc",
        1746334800000,
        "30.00",
        "GBP",
        "Lunch with friends",
        "GB29NWBK60161331926819",
        "Transport",
        "Fuel"
    ],
    [
        "79392813-46ba-11f0-9268-a08cfddbfccc",
        1746450000000,
        "45.00",
        "GBP",
        "Fuel for car",
        "GB29NWBK60161331926819",
        "Transport",
        "Fuel"
    ],
    [
        "79393a14-46ba-11f0-9268-a08cfddbfccc",
        1748163600000,
        "25.00",
        "GBP",
        "Cafe lunch",
        "GB29NWBK60161331926819",
        "Transport",
        "Fuel"
    ]
    ],
    "error": null
}*/
