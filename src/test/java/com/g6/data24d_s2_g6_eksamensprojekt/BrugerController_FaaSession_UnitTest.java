package com.g6.data24d_s2_g6_eksamensprojekt;

import com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger.Stilling;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // fra MockitoExtension fra pom.xml
public class BrugerController_FaaSession_UnitTest {



    @InjectMocks
    BrugerController brugerController; // laver en test version af en klasse

    @Test
    @DisplayName("faaSessionHappyFlow")
    public void faaSessionHappyFlow(){ // her testes hvis metoden lykkes.
        // Assumptions  // laver en test version af en de klasser som der laves objekter af.
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        Model model = new ExtendedModelMap();
        Bruger mockBruger = mock(Bruger.class);
        // er det samme som if statements. bare hvor man skriver boolean før runtime før runtime.
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("aktivBruger")).thenReturn(mockBruger);
        when(mockBruger.erStilling("data")).thenReturn(true);
        when(session.getAttribute("valuta")).thenReturn(true); //true for euro, false for dkk

        // Execution//bruger til at teste metoden og få resultant.
        HttpSession result = BrugerController.faaSession(request,model,Stilling.DATA);

        // Validations// JUnit for at teste om vi har fået hvad vi har forventet
        assertNotNull(result);
        assertEquals(mockBruger, model.getAttribute("aktivBruger"));
        assertEquals(true,model.getAttribute("valuta"));

    }
    @Test
    @DisplayName("faaSessionUnhappyFlow")
    public void faaSessionUnhappyFlow(){ // her testes hvis metoden mislykkes
        // Assumptions
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        Model model = new ExtendedModelMap();
        Bruger mockBruger = mock(Bruger.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("aktivBruger")).thenReturn(mockBruger);
        when(mockBruger.erStilling("test")).thenReturn(null); //Kun DEMO, DATA, SKADE, FORRETNING, er tilladte stillinger

        // Execution
        HttpSession result = BrugerController.faaSession(request,model);

        // Validations
        assertNull(result);
        assertNull(model.getAttribute("aktivBruger"));
        assertNull(model.getAttribute("valuta"));
    }

}
