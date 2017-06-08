package com.example.caro.microadmin;

/**
 * Created by Caro on 24/05/2017.
 */
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UIProductoTest {

    private String codigo;
    private String nombre;
    private String preciounitario;
    private String costomanufactura;
    private String cantidad;

    @Rule
    public ActivityTestRule<ProductoActivity> mActivityRule = new ActivityTestRule<>(
            ProductoActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        codigo = "72";
        nombre = "Pantalla";
        preciounitario="30000";
        costomanufactura="20000";
        cantidad = "34";

    }

    @Test
    public void camposVacios_mostrarMensajes() {
        codigo = "";
        nombre = "";
        preciounitario="";
        costomanufactura="";
        cantidad = "";

        onView(withId(R.id.tf_codigo))
                .perform(typeText(codigo), closeSoftKeyboard());

        onView(withId(R.id.tv_nombre_producto))
                .perform(typeText(nombre), closeSoftKeyboard());

        onView(withId(R.id.tf_precio_unidad))
                .perform(typeText(preciounitario), closeSoftKeyboard());

        onView(withId(R.id.tf_costo_manufactura))
                .perform(typeText(costomanufactura), closeSoftKeyboard());

        onView(withId(R.id.tf_cantifdad))
                .perform(typeText(cantidad), closeSoftKeyboard());

        onView(withId(R.id.bt_guardar)).perform(click());
        onView(withId(R.id.tf_codigo)).check(matches(hasErrorText("Este campo es requerido")));
        onView(withId(R.id.tv_nombre_producto)).check(matches(hasErrorText("Este campo es requerido")));
        onView(withId(R.id.tf_precio_unidad)).check(matches(hasErrorText("Este campo es requerido")));
        onView(withId(R.id.tf_costo_manufactura)).check(matches(hasErrorText("Este campo es requerido")));
        onView(withId(R.id.tf_cantifdad)).check(matches(hasErrorText("Este campo es requerido")));
    }






}
