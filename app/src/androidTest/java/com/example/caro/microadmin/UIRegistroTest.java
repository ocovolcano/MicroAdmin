package com.example.caro.microadmin;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import android.support.test.rule.ActivityTestRule;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Created by Caro on 24/05/2017.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UIRegistroTest {
    private String usuario;
    private String contrasena;
    private String nombre;

    @Rule
    public ActivityTestRule<RegistroActivity> mActivityRule = new ActivityTestRule<>(
            RegistroActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        usuario = "carooco@gmail.com";
        contrasena = "Carolina";
        nombre = "Caro";

    }

    @Test
    public void camposVacios_mostrarMensajes() {
        usuario = "";
        contrasena = "";

        onView(withId(R.id.tf_correo))
                .perform(typeText(usuario), closeSoftKeyboard());

        onView(withId(R.id.tf_contrasena))
                .perform(typeText(contrasena), closeSoftKeyboard());

        onView(withId(R.id.tf_nombre))
                .perform(typeText(nombre), closeSoftKeyboard());


        onView(withId(R.id.bt_registrarme)).perform(click());
        onView(withId(R.id.tf_correo)).check(matches(hasErrorText("Este campo es requerido")));
        onView(withId(R.id.tf_contrasena)).check(matches(hasErrorText("Este campo es requerido")));
    }

    @Test
    public void correoInvalido_mostrarMensajes() {
        usuario = "red";


        onView(withId(R.id.tf_correo))
                .perform(typeText(usuario), closeSoftKeyboard());

        onView(withId(R.id.tf_contrasena))
                .perform(typeText(contrasena), closeSoftKeyboard());

        onView(withId(R.id.tf_nombre))
                .perform(typeText(nombre), closeSoftKeyboard());


        onView(withId(R.id.bt_registrarme)).perform(click());
        onView(withId(R.id.tf_correo)).check(matches(hasErrorText("El correo electrónico no es válido")));

    }




}
