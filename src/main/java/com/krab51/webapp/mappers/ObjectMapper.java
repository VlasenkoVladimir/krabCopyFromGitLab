package com.krab51.webapp.mappers;

import com.krab51.webapp.domain.Client;
import com.krab51.webapp.domain.Company;
import com.krab51.webapp.domain.KrabOrder;
import com.krab51.webapp.domain.KrabReport;
import com.krab51.webapp.domain.Operator;
import com.krab51.webapp.domain.Trap;
import com.krab51.webapp.domain.TrapOwner;
import com.krab51.webapp.dto.ClientDto;
import com.krab51.webapp.dto.CompanyDto;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.dto.KrabReportDto;
import com.krab51.webapp.dto.OperatorDto;
import com.krab51.webapp.dto.TrapDto;
import com.krab51.webapp.dto.TrapOwnerDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class ObjectMapper {
    public abstract Client clientDtoToClient(ClientDto clientDto);

    public abstract ClientDto clientToClientDto(Client client);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    public abstract void updateClientFromClientDto(ClientDto clientDto, @MappingTarget Client client);

    public abstract KrabOrder krabOrderDtoToKrabOrder(KrabOrderDto krabOrderDto);

    public abstract KrabOrderDto krabOrderToKrabOrderDto(KrabOrder krabOrder);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    public abstract void updateKrabOrderFromKrabOrderDto(KrabOrderDto krabOrderDto, @MappingTarget KrabOrder krabOrder);

    public abstract KrabReport krabReportDtoToKrabReport(KrabReportDto krabReportDto);

    public abstract KrabReportDto krabReportToKrabReportDto(KrabReport krabReport);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    public abstract void updateKrabReportFromKrabReportDto(KrabReportDto krabReportDto, @MappingTarget KrabReport krabReport);

    public abstract Operator operatorDtoToOperator(OperatorDto operatorDto);

    public abstract OperatorDto operatorToOperatorDto(Operator operator);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    public abstract void updateOperatorFromOperatorDto(OperatorDto operatorDto, @MappingTarget Operator operator);

    public abstract TrapOwner trapOwnerDtoToTrapOwner(TrapOwnerDto trapOwnerDto);

    public abstract TrapOwnerDto trapOwnerToTrapOwnerDto(TrapOwner trapOwner);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    public abstract void updateTrapOwnerFromTrapOwnerDto(TrapOwnerDto trapOwnerDto, @MappingTarget TrapOwner trapOwner);

    public abstract Trap trapDtoToTrap(TrapDto trapDto);

    public abstract TrapDto trapToTrapDto(Trap trap);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    public abstract void updateTrapFromTrapDto(TrapDto trapDto, @MappingTarget Trap trap);

    public abstract CompanyDto companyToCompanyDto (Company company);

    public abstract Company companyDtoToCompany (CompanyDto companyDto);
}