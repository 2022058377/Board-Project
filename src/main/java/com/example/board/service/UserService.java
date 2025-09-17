package com.example.board.service;

import com.example.board.exception.follow.FollowAlreadyExistsException;
import com.example.board.exception.follow.FollowNotFoundException;
import com.example.board.exception.follow.InvalidFollowException;
import com.example.board.exception.post.PostNotFountException;
import com.example.board.exception.user.UserAlreadyExistsException;
import com.example.board.exception.user.UserNotAllowedException;
import com.example.board.exception.user.UserNotFoundException;
import com.example.board.model.entity.FollowEntity;
import com.example.board.model.entity.LikeEntity;
import com.example.board.model.entity.PostEntity;
import com.example.board.model.entity.UserEntity;
import com.example.board.model.user.*;
import com.example.board.repository.FollowEntityRepository;
import com.example.board.repository.LikeEntityRepository;
import com.example.board.repository.PostEntityRepository;
import com.example.board.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired private UserEntityRepository userEntityRepository;

    @Autowired private FollowEntityRepository followEntityRepository;

    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Autowired private JwtService jwtService;

    @Autowired private PostEntityRepository postEntityRepository;

    @Autowired private LikeEntityRepository likeEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User signUp(String username, String password) {
        userEntityRepository
                .findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });
        var savedUser = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.from(savedUser);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {

        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(passwordEncoder.matches(password, userEntity.getPassword())) {
            var accessToken = jwtService.generateAccessToken(userEntity);

            return new UserAuthenticationResponse(accessToken);

        } else {
            throw new UserNotFoundException(username);
        }
    }

    public List<User> getUsers(String query, UserEntity currentUser) {

        List<UserEntity> userEntities;

        if(query != null && !query.isBlank()) {
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }

        return userEntities.stream()
                .map(user -> getUserWithFollowingStatus(user, currentUser))
                .toList();
    }

    public User getUser(String username, UserEntity currentUser) {

        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return getUserWithFollowingStatus(userEntity, currentUser);
    }

    private User getUserWithFollowingStatus(UserEntity userEntity, UserEntity currentUser) {

        var isFollowing = followEntityRepository.findByFollowerAndFollowing(currentUser, userEntity).isPresent();

        return User.from(userEntity, isFollowing);
    }

    public User updateUser(String username, UserPatchRequestBody body, UserEntity user) {

        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(!userEntity.equals(user)) {
            throw new UserNotAllowedException();
        }

        if(body.description() != null) {
            userEntity.setDescription(body.description());
        }

        return User.from(userEntityRepository.save(userEntity));

    }

    @Transactional
    public User follower(String username, UserEntity user) {

        var following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(following.equals(user)) {
            throw new InvalidFollowException("A user cannot follow yourself");
        }

        followEntityRepository.findByFollowerAndFollowing(user, following)
                .ifPresent(follow -> {
                    throw new FollowAlreadyExistsException(user, following);
                });

        followEntityRepository.save(FollowEntity.of(user, following));

        following.setFollowersCount(following.getFollowersCount() + 1);
        user.setFollowingsCount(user.getFollowingsCount() + 1);
        userEntityRepository.saveAll(List.of(following, user));

        return User.from(following, true);
    }

    @Transactional
    public User unFollower(String username, UserEntity user) {

        var following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if(following.equals(user)) {
            throw new InvalidFollowException("A user cannot follow yourself");
        }

        var followEntity = followEntityRepository.findByFollowerAndFollowing(user, following)
                .orElseThrow(() -> new FollowNotFoundException(user, following));

        followEntityRepository.delete(followEntity);
        following.setFollowersCount(Math.max(0, following.getFollowersCount() - 1));
        user.setFollowingsCount(Math.max(0, user.getFollowingsCount() - 1));
        userEntityRepository.saveAll(List.of(following, user));

        return User.from(following, false);
    }

    // 내가 팔로우 하는 User 목록
    public List<Follower> getFollowersByUsername(String username, UserEntity currentUser) {

        var following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var followEntities = followEntityRepository.findByFollowing(following);

        return followEntities.stream().map(
                follow -> Follower.from(
                        getUserWithFollowingStatus(follow.getFollower(), currentUser),
                        follow.getCreatedAt())
                )
                .toList();
    }

    // 나를 팔로우 하는 User 목록
    public List<User> getFollowingsByUsername(String username, UserEntity currentUser) {

        var follower = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var followEntities = followEntityRepository.findByFollower(follower);

        return followEntities.stream().map(
                follow -> getUserWithFollowingStatus(follow.getFollowing(), currentUser))
                .toList();
    }

    public List<LikedUser> getLikedUsersByPostId(Long postId, UserEntity currentUser) {

        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        var likedEntities = likeEntityRepository.findByPost(postEntity);

        return likedEntities.stream().map(
                likeEntity -> getLikedUserWithFollowingStatus(
                        likeEntity, postEntity, currentUser
                ))
                .toList();
    }

    public List<LikedUser> getLikedUsersByUser(String username, UserEntity currentUser) {

        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var postEntities = postEntityRepository.findByUser(userEntity);

        return postEntities.stream()
                .flatMap(postEntity ->
                        likeEntityRepository.findByPost(postEntity).stream()
                                .map(
                                        likeEntity ->
                                                getLikedUserWithFollowingStatus(likeEntity, postEntity, currentUser)
                                )
                )
                .toList();
    }

    private LikedUser getLikedUserWithFollowingStatus(
            LikeEntity likeEntity, PostEntity postEntity, UserEntity currentUser) {

        var likedUser = likeEntity.getUser();
        var userWithFollowingStatus = getUserWithFollowingStatus(likedUser, currentUser);
        return LikedUser.from(
                userWithFollowingStatus,
                postEntity.getPostId(),
                likeEntity.getCreatedAt());
    }
}
