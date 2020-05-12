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

    @Override
    public User register(String username, String password){
        User user = new User(username,password);
        entityManager.persist(user);
        return user;
    }

    @Override
    public User login(String username, String password){
        try{
            User user = (User) entityManager.createNamedQuery("User.getLogin")
                    .setParameter("username",username)
                    .setParameter("password",new BCryptPasswordEncoder(12).encode(password))
                    .getSingleResult();
            return user;
        }catch (NoResultException e){
            throw new UserException("User does not exist in the database..");
        }
    }

}
