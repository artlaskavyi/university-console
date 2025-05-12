package com.arty.university_console.console;

import com.arty.university_console.model.Degree;
import com.arty.university_console.service.DepartmentService;
import com.arty.university_console.service.LectorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ConsoleUI {

    private final DepartmentService departmentService;
    private final LectorService lectorService;
    private final Scanner scanner;
    private enum Command {HEAD, STATISTICS, AVG_SALARY, EMPLOYEE_COUNT, SEARCH, UNKNOWN};

    public ConsoleUI(DepartmentService departmentService, LectorService lectorService) {
        this.departmentService = departmentService;
        this.lectorService = lectorService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to University Console");

        while (true) {
            System.out.println("Enter your command:\n" +
                    "HEAD, STATISTICS, AVG_SALARY, EMPLOYEE_COUNT, SEARCH, EXIT (case insensitive)\n)");
            String input = scanner.nextLine().trim().toUpperCase();

            if(input.equalsIgnoreCase("exit")){
                System.out.println("Bye!");
                break;
            }

            Command command = getCommandFromString(input);
            processCommand(command);
        }
        scanner.close();
    }

    private Command getCommandFromString(String commandString) {
        try {
            return Command.valueOf(commandString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    private void processCommand(Command command) {
        try{
            switch (command) {
                case HEAD:
                    System.out.println("Executing HEAD command...");
                    handleHeadCommand();
                    break;
                case STATISTICS:
                    System.out.println("Executing STATISTICS command...");
                    handleStatisticsCommand();
                    break;
                case AVG_SALARY:
                    System.out.println("Executing AVG_SALARY command...");
                    handleAverageSalaryCommand();
                    break;
                case EMPLOYEE_COUNT:
                    System.out.println("Executing EMPLOYEE_COUNT command...");
                    handleEmployeeCountCommand();
                    break;
                case SEARCH:
                    System.out.println("Executing SEARCH command...");
                    handleSearchCommand();
                    break;
                case UNKNOWN:
                    System.out.println("Unknown command. Please enter a valid command.");
                    break;
            }
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void handleHeadCommand() {
        System.out.println("Enter the name of the department (case sensitive):");
        String departmentName = scanner.nextLine().trim();

        String headFullName = departmentService.getDepartmentHeadFullName(departmentName);
        String s  = String.format("Head of %s department is %s", departmentName, headFullName);

        System.out.println(s);
    }

    private void handleStatisticsCommand() {
        System.out.println("Enter the name of the department (case sensitive):");
        String departmentName = scanner.nextLine().trim();

        Map<String, Long> statistics = departmentService.getDepartmentStatistics(departmentName);

        for (Map.Entry<String, Long> entry : statistics.entrySet()) {
            String degreeName = entry.getKey();
            Long count = entry.getValue();
            System.out.println(degreeName + " - " + count);
        }
    }

    private void handleAverageSalaryCommand() {
        System.out.println("Enter the name of the department (case sensitive):");
        String departmentName = scanner.nextLine().trim();

        BigDecimal salary = departmentService.getDepartmentAverageSalary(departmentName);

        String s = String.format("Average salary of %s department is %s", departmentName, salary);
        System.out.println(s);
    }

    private void handleEmployeeCountCommand() {
        System.out.println("Enter the name of the department (case sensitive):");
        String departmentName = scanner.nextLine().trim();

        Long employeeCount= departmentService.getDepartmentEmployeeCount(departmentName);

        System.out.println(employeeCount);

    }

    private void handleSearchCommand() {
        System.out.println("Enter the template to search for:");
        String departmentName = scanner.nextLine().trim();

        List<String> results = lectorService.performGlobalSearch(departmentName);

        if (results.isEmpty()) {
            System.out.println("No lectors found matching the template.");
        } else {
            String joinedResults = String.join(", ", results);
            System.out.println("Matching lectors: " + joinedResults);
        }
    }

}
