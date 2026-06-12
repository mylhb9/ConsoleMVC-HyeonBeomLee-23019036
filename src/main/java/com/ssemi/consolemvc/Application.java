package com.ssemi.consolemvc;

import com.ssemi.consolemvc.controller.SampleController;
import com.ssemi.consolemvc.repository.InMemorySampleRepository;
import com.ssemi.consolemvc.repository.SampleRepository;
import com.ssemi.consolemvc.service.SampleService;
import com.ssemi.consolemvc.view.MainView;
import com.ssemi.consolemvc.view.SampleView;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        SampleRepository repository = new InMemorySampleRepository();
        SampleService service = new SampleService(repository);
        SampleView sampleView = new SampleView(scanner);
        SampleController controller = new SampleController(service, sampleView);
        MainView mainView = new MainView(scanner, controller);

        mainView.run();
        scanner.close();
    }
}
