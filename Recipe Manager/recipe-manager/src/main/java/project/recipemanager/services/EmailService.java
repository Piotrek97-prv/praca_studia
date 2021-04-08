package project.recipemanager.services;

import project.recipemanager.model.User;

public interface EmailService {
    void sendActivationEmailToUser(User userToActivate);
}
