package LagranaExam.Services;

import LagranaExam.Models.AppUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<AppUser> appUsers;

    @PostConstruct
    public void init() throws IOException {
        appUsers = new ArrayList<>();
        File file = new File("demo/data/users.csv"); // path relative to project root

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        reader.readLine(); // skip header
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            AppUser appUser = new AppUser();
            appUser.setUsername(parts[0]);
            appUser.setPassword(parts[1]);
            appUsers.add(appUser);
        }
    }

    public AppUser findByUsername(String username) {
        return appUsers.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void save(AppUser appUser) {
        // Method left empty intentionally
    }
}
