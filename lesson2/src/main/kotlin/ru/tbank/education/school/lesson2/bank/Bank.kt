package ru.tbank.education.school.lesson2.bank

class Bank {
    var accountSeq = 1
    var clientSeq = 1

    private val clients: MutableList<Client> = mutableListOf()
    private val accounts: MutableList<Account> = mutableListOf()

    fun addClient(clientFullName: String) {
        val newClient = Client(
        )
        clients.add(newClient)
    }

    fun addAccount(clientId: String) {
        val newAccount = Account(
            balance = 0.0,
            customerId = clientId
        )
        accounts.add(newAccount)
    }

    fun transfer(fromAccountId: String, toAccountId: String, amount: Double) {


        if (ok) {
        }
    }
}