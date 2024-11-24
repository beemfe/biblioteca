/*
 * Nome: Felipe Bernardes Cisilo
 * RA: 1110482413017
 */
package br.edu.fateczl.biblioteca.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import br.edu.fateczl.biblioteca.R;
import br.edu.fateczl.biblioteca.dao.ExemplarDAO;
import br.edu.fateczl.biblioteca.ui.fragments.AlunoFragment;
import br.edu.fateczl.biblioteca.ui.fragments.HomeFragment;
import br.edu.fateczl.biblioteca.ui.fragments.LivroFragment;
import br.edu.fateczl.biblioteca.ui.fragments.RevistaFragment;
import br.edu.fateczl.biblioteca.ui.fragments.AluguelFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private ExemplarDAO exemplarDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exemplarDAO = new ExemplarDAO(this);
        setupNavigation();
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void setupNavigation() {
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.nav_aluno) {
                fragment = new AlunoFragment();
            } else if (itemId == R.id.nav_livro) {
                fragment = new LivroFragment();
            } else if (itemId == R.id.nav_revista) {
                fragment = new RevistaFragment();
            } else if (itemId == R.id.nav_aluguel) {
                if (exemplarDAO.listar().isEmpty()) {
                    Toast.makeText(this, "Não há exemplares cadastrados!",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                fragment = new AluguelFragment();
            }

            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(currentFragment instanceof HomeFragment)) {
            loadFragment(new HomeFragment());
            bottomNav.setSelectedItemId(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }
}