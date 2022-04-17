package com.github.flooooooooooorian.meinkochbuch.security.models;

import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;
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
    @Column(nullable = false)
    private String id;

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

    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy("name")
    private List<Recipe> favoriteRecipes;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @OrderBy("relevance")
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @OrderBy("name")
    private List<Cookbook> cookbooks;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Image> images;

    private Instant joinedAt;

    public static ChefUser ofId(String userId) {
        return ChefUser.builder().id(userId).build();
    }
}
