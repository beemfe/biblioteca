/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DB_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static LocalDate stringToDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DEFAULT_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String dateToString(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DEFAULT_FORMATTER);
    }

    public static String dateToDBString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DB_FORMATTER);
    }

    public static LocalDate dbStringToDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DB_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDate calcularDataDevolucao() {
        return LocalDate.now().plusDays(7);
    }

    public static boolean isEmprestimoAtrasado(LocalDate dataRetirada, LocalDate dataDevolucao) {
        if (dataRetirada == null || dataDevolucao == null) {
            return false;
        }
        LocalDate dataPrevista = dataRetirada.plusDays(7);
        return LocalDate.now().isAfter(dataPrevista) && dataDevolucao == null;
    }

    public static long calcularDiasAtraso(LocalDate dataRetirada) {
        if (dataRetirada == null) {
            return 0;
        }
        LocalDate dataPrevista = dataRetirada.plusDays(7);
        LocalDate hoje = LocalDate.now();
        if (hoje.isAfter(dataPrevista)) {
            return dataPrevista.until(hoje).getDays();
        }
        return 0;
    }

    public static boolean isDataValida(String dateStr) {
        try {
            LocalDate.parse(dateStr, DEFAULT_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}