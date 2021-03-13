package ru.sfedu.deals.lab5.provider;

import ru.sfedu.deals.lab5.Address;
import ru.sfedu.deals.lab5.Company;
import ru.sfedu.deals.lab5.Deal;
import ru.sfedu.deals.lab5.User;

import java.util.List;
import java.util.Optional;

public interface IProvider {

  Optional<List<Address>> selectAllAddress();

  Optional<List<User>> selectAllUsers();

  Optional<List<Deal>> selectAllDeals();

  Optional<List<Company>> selectAllCompanies();

}
