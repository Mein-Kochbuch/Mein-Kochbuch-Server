package com.github.flooooooooooorian.meinkochbuch.security.models;

import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.security.models.oauth.OAuthDataApple;
import com.github.flooooooooooorian.meinkochbuch.security.models.oauth.OAuthDataGoogle;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChefUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotEmpty
    @Size(min = 8)
    private String password;

    @Email
    @Column(unique = true)
    private String username;

    @ElementCollection(targetClass = ChefAuthorities.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<ChefAuthorities> authorities;

    @Column(nullable = false)
    private boolean accountNonExpired;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private boolean credentialsNonExpired;

    @Column(nullable = false)
    private boolean enabled;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToMany()
    private List<Recipe> favoriteRecipes;

    @OneToOne
    private OAuthDataApple oAuthDataApple;
    @OneToOne
    private OAuthDataGoogle oAuthDataGoogle;


    @OneToMany(mappedBy = "owner")
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "owner")
    private List<Cookbook> cookbooks;

    @OneToMany(mappedBy = "owner")
    private List<Image> images;

    public static ChefUser ofId(Long userId) {
        return ChefUser.builder().id(userId).build();
    }
}
