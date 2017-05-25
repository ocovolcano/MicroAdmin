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
public class UILoginTest {
    private String usuario;
    private String contrasena;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        usuario = "carooco30@gmail.com";
        contrasena = "Carolina";

    }

    @Test
    public void camposVacios_mostrarMensajes() {
        usuario = "";
        contrasena = "";
        onView(withId(R.id.email))
                .perform(typeText(usuario), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(contrasena), closeSoftKeyboard());

        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Este campo es requerido")));
        onView(withId(R.id.password)).check(matches(hasErrorText("Este campo es requerido")));

    }


    @Test
    public void contrasenaCorta_mostrarMensaje() {
        contrasena = "12";
        onView(withId(R.id.email))
                .perform(typeText(usuario), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(contrasena), closeSoftKeyboard());

        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("La contraseña es muy corta")));

    }

    @Test
    public void correoInvalido_mostrarMensaje() {
        usuario = "12";
        onView(withId(R.id.email))
                .perform(typeText(usuario), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(contrasena), closeSoftKeyboard());


        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText("El correo electrónico no es válido")));

    }

    @Test
    public void llenarFormulario_ingresarActivityInventario() {
        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(usuario), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(contrasena), closeSoftKeyboard());

        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(allOf(withId(R.id.fab), isDisplayed()));


    }



}


