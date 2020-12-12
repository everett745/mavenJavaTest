/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.maven1.dataProviders;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.maven1.enums.DealStatus;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DataProviderXML implements DataProvider {
    private static Logger log = LogManager.getLogger(DataProviderXML.class);


    @Override
    public RequestStatuses createUser(@NotNull String name, @NotNull String phone, @NotNull Address address) {
        return null;
    }

    @Override
    public Optional<User> getUser(@NotNull UUID userId) {
        return Optional.empty();
    }

    @Override
    public RequestStatuses editUser(@NotNull User user) {
        return null;
    }

    @Override
    public RequestStatuses deleteUser(@NotNull UUID userId) {
        return null;
    }

    @Override
    public Optional<List<User>> getUsers() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Address>> getAddresses() {
        return Optional.empty();
    }

    @Override
    public Optional<Address> getAddress(@NotNull long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Address> getAddress(@NotNull String city) {
        return Optional.empty();
    }

    @Override
    public Optional<Queue> getQueue(@NotNull UUID id) {
        return Optional.empty();
    }

    @Override
    public RequestStatuses createDeal(@NotNull UUID userId, @NotNull String name, @NotNull String description, @NotNull Address address, @NotNull DealTypes dealType, @NotNull ObjectTypes objectType, @NotNull String price) {
        return null;
    }

    @Override
    public RequestStatuses createDeal(@NotNull UUID userId, @NotNull String name, @NotNull String description, @NotNull Address address, @NotNull DealStatus currentStatus, @NotNull DealTypes dealType, @NotNull ObjectTypes objectType, @NotNull String price) {
        return null;
    }

    @Override
    public Optional<List<PublicDeal>> getGlobalDeals(@NotNull UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Deal>> getMyDeals(@NotNull UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Deal> manageDeal(@NotNull UUID id) {
        return Optional.empty();
    }

    @Override
    public RequestStatuses removeDeal(@NotNull UUID id) {
        return null;
    }

    @Override
    public RequestStatuses updateDeal(@NotNull Deal deal) {
        return null;
    }

    @Override
    public RequestStatuses setStatus(@NotNull UUID id, @NotNull DealStatus newStatus) {
        return null;
    }

    @Override
    public RequestStatuses addDealRequest(@NotNull UUID userId, @NotNull UUID id) {
        return null;
    }

    @Override
    public Optional<Queue> getDealQueue(@NotNull UUID id) {
        return Optional.empty();
    }

    @Override
    public RequestStatuses manageDealRequest(@NotNull UUID userId, @NotNull UUID id, @NotNull boolean accept) {
        return null;
    }

    @Override
    public RequestStatuses acceptDealRequest(@NotNull UUID userId, @NotNull UUID id) {
        return null;
    }

    @Override
    public RequestStatuses refuseDealRequest(@NotNull UUID userId, @NotNull UUID id) {
        return null;
    }

    @Override
    public RequestStatuses addDealPerformer(@NotNull UUID userId, @NotNull UUID id) {
        return null;
    }

    @Override
    public Optional<Queue> getMyQueue(@NotNull UUID userId) {
        return Optional.empty();
    }

    @Override
    public RequestStatuses manageDealPerform(@NotNull UUID userId, @NotNull UUID id, boolean accept) {
        return null;
    }

    @Override
    public RequestStatuses acceptDealPerform(@NotNull UUID userId, @NotNull UUID id) {
        return null;
    }

    @Override
    public RequestStatuses refuseDealPerform(@NotNull UUID userId, @NotNull UUID id) {
        return null;
    }

    @Override
    public void deleteAll() {}
}
