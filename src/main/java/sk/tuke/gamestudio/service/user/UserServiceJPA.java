package sk.tuke.gamestudio.service.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Transactional
public class UserServiceJPA implements UserService{

    @PersistenceContext
    private EntityManager entityManager;

    private User user;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public void register(String username, String password){
        User user = new User(username, encoder.encode(password));
        entityManager.persist(user);
    }

    @Override
    public User login(String username){
        try{
            User loggedUser = (User) entityManager.createNamedQuery("User.getLogin")
                    .setParameter("username",username)
                    .getSingleResult();
            user = loggedUser;
            return loggedUser;
        }catch (NoResultException e){
            throw new UserException("User does not exist in the database..");
        }
    }

    @Override
    public boolean isPasswordVerified(String username, String password) {
        try{
            String hash = (String) entityManager.createNamedQuery("User.getHash")
                    .setParameter("username",username)
                    .getSingleResult();
            return encoder.matches(password,hash);
        }catch (NoResultException e){
            throw new UserException("Password does not match.");
        }
    }

    public User getUser(){
        return user;
    }


}
