package com.arty.university_console.console;

import com.arty.university_console.service.DepartmentService;
import com.arty.university_console.service.LectorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleUI {

    private final DepartmentService departmentService;
    private final LectorService lectorService;
    private final Scanner scanner;

    public ConsoleUI(DepartmentService departmentService, LectorService lectorService) {
        this.departmentService = departmentService;
        this.lectorService = lectorService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to University Console");

        while (true) {
            displayAllowedCommands();
            System.out.println(CommandConstants.PROMPT_MESSAGE);
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Bye!");
                break;
            }

            processInput(input);
            System.out.println();
        }
        scanner.close();
    }

    private void displayAllowedCommands() {
        System.out.println("Available commands:");
        System.out.println("- " + CommandConstants.WHO_IS_HEAD_COMMAND + "{department_name}");
        System.out.println("- " + CommandConstants.SHOW_STATISTICS_COMMAND + "{department_name}" + CommandConstants.STATISTICS_SUFFIX);
        System.out.println("- " + CommandConstants.SHOW_AVG_SALARY_COMMAND + "{department_name}");
        System.out.println("- " + CommandConstants.SHOW_EMPLOYEE_COUNT_COMMAND + "{department_name}");
        System.out.println("- " + CommandConstants.GLOBAL_SEARCH_COMMAND + "{template}");
        System.out.println("- exit");
    }

    private void processInput(String input) {

        if (input == null || input.isBlank()) {
            System.out.println("Input cannot be empty.");
            return;
        }

        try {
            if (input.startsWith(CommandConstants.WHO_IS_HEAD_COMMAND)) {
                String departmentName = input.substring(CommandConstants.WHO_IS_HEAD_COMMAND.length()).trim();
                handleHeadCommand(departmentName);
            } else if (input.startsWith(CommandConstants.SHOW_STATISTICS_COMMAND) &&
                    input.endsWith(CommandConstants.STATISTICS_SUFFIX) &&
                    input.length() > CommandConstants.SHOW_STATISTICS_COMMAND.length() + CommandConstants.STATISTICS_SUFFIX.length() + 1) {
                String departmentName = input.substring(CommandConstants.SHOW_STATISTICS_COMMAND.length(), input.indexOf(CommandConstants.STATISTICS_SUFFIX)).trim();
                handleStatisticsCommand(departmentName);
            } else if (input.startsWith(CommandConstants.SHOW_AVG_SALARY_COMMAND)) {
                String departmentName = input.substring(CommandConstants.SHOW_AVG_SALARY_COMMAND.length()).trim();
                handleAverageSalaryCommand(departmentName);
            } else if (input.startsWith(CommandConstants.SHOW_EMPLOYEE_COUNT_COMMAND)) {
                String departmentName = input.substring(CommandConstants.SHOW_EMPLOYEE_COUNT_COMMAND.length()).trim();
                handleEmployeeCountCommand(departmentName);
            } else if (input.startsWith(CommandConstants.GLOBAL_SEARCH_COMMAND)) {
                String template = input.substring(CommandConstants.GLOBAL_SEARCH_COMMAND.length()).trim();
                handleSearchCommand(template);
            } else {
                System.out.println("Unknown command. Please enter a valid command.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleHeadCommand(String departmentName) {
        String headFullName = departmentService.getDepartmentHeadFullName(departmentName);
        System.out.printf("Head of %s department is %s%n", departmentName, headFullName);
    }

    private void handleStatisticsCommand(String departmentName) {
        Map<String, Long> statistics = departmentService.getDepartmentStatistics(departmentName);
        for (Map.Entry<String, Long> entry : statistics.entrySet()) {
            String degreeName = entry.getKey();
            Long count = entry.getValue();
            System.out.println(degreeName + " - " + count);
        }
    }

    private void handleAverageSalaryCommand(String departmentName) {
        BigDecimal salary = departmentService.getDepartmentAverageSalary(departmentName);
        System.out.printf("The average salary of %s is %s%n", departmentName, salary);
    }

    private void handleEmployeeCountCommand(String departmentName) {
        Long employeeCount = departmentService.getDepartmentEmployeeCount(departmentName);
        System.out.println(employeeCount);
    }

    private void handleSearchCommand(String searchTemplate) {
        List<String> results = lectorService.performGlobalSearch(searchTemplate);
        if (results.isEmpty()) {
            System.out.println("No lectors found matching the template.");
        } else {
            System.out.println(String.join(", ", results));
        }
    }

}
