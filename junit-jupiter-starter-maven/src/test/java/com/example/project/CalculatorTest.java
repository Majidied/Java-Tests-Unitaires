/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Calculator Tests")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Nested
    @DisplayName("Cas normaux")
    class NormalCases {

        @Test
        @DisplayName("Addition de deux nombres positifs")
        void testAdditionPositifs() {
            assertEquals(5, calculator.add(2, 3), "2 + 3 devrait égaler 5");
            assertEquals(100, calculator.add(50, 50), "50 + 50 devrait égaler 100");
        }

        @Test
        @DisplayName("Addition avec zéro")
        void testAdditionAvecZero() {
            assertEquals(10, calculator.add(10, 0), "10 + 0 devrait égaler 10");
            assertEquals(0, calculator.add(0, 0), "0 + 0 devrait égaler 0");
            assertEquals(-5, calculator.add(-5, 0), "-5 + 0 devrait égaler -5");
        }

        @Test
        @DisplayName("Addition de nombres négatifs")
        void testAdditionNegatifs() {
            assertEquals(-5, calculator.add(-2, -3), "-2 + (-3) devrait égaler -5");
            assertEquals(-10, calculator.add(-15, 5), "-15 + 5 devrait égaler -10");
        }

        @ParameterizedTest(name = "Addition paramétrée: {0} + {1} = {2}")
        @CsvSource({
            "1, 1, 2",
            "10, 20, 30",
            "-5, 5, 0",
            "-10, -20, -30",
            "100, 200, 300"
        })
        void testAdditionParametree(int a, int b, int expected) {
            assertEquals(expected, calculator.add(a, b),
                () -> String.format("%d + %d devrait égaler %d", a, b, expected));
        }
    }

    @Nested
    @DisplayName("Cas limites")
    class EdgeCases {

        @Test
        @DisplayName("Addition avec Integer.MAX_VALUE")
        void testAdditionMaxValue() {
            // Test avec la valeur maximale d'un int
            int result = calculator.add(Integer.MAX_VALUE, 0);
            assertEquals(Integer.MAX_VALUE, result, "MAX_VALUE + 0 devrait égaler MAX_VALUE");

            // Test d'overflow (comportement défini en Java)
            result = calculator.add(Integer.MAX_VALUE, 1);
            assertEquals(Integer.MIN_VALUE, result, "MAX_VALUE + 1 cause un overflow vers MIN_VALUE");
        }

        @Test
        @DisplayName("Addition avec Integer.MIN_VALUE")
        void testAdditionMinValue() {
            int result = calculator.add(Integer.MIN_VALUE, 0);
            assertEquals(Integer.MIN_VALUE, result, "MIN_VALUE + 0 devrait égaler MIN_VALUE");

            // Test d'overflow négatif
            result = calculator.add(Integer.MIN_VALUE, -1);
            assertEquals(Integer.MAX_VALUE, result, "MIN_VALUE + (-1) cause un overflow vers MAX_VALUE");
        }

        @Test
        @DisplayName("Addition de grands nombres")
        void testAdditionGrandsNombres() {
            assertEquals(2000000, calculator.add(1000000, 1000000), "1M + 1M devrait égaler 2M");
            assertEquals(-2000000, calculator.add(-1000000, -1000000), "-1M + (-1M) devrait égaler -2M");
        }

        @ParameterizedTest(name = "Test avec valeur limite: {0}")
        @ValueSource(ints = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 1, -1})
        void testAdditionAvecValeursLimites(int value) {
            assertEquals(value, calculator.add(value, 0),
                () -> String.format("%d + 0 devrait égaler %d", value, value));
        }
    }

    @Nested
    @DisplayName("Tests d'exceptions et comportements inattendus")
    class ExceptionCases {

        @Test
        @DisplayName("Vérification que l'addition ne lance pas d'exceptions")
        void testAdditionNeLancePasException() {
            assertDoesNotThrow(() -> calculator.add(1, 1), "L'addition ne devrait pas lancer d'exception");
            assertDoesNotThrow(() -> calculator.add(Integer.MAX_VALUE, 1), "L'addition avec overflow ne devrait pas lancer d'exception");
            assertDoesNotThrow(() -> calculator.add(Integer.MIN_VALUE, -1), "L'addition avec underflow ne devrait pas lancer d'exception");
        }

        @Test
        @DisplayName("Test de cohérence des résultats")
        void testCoherenceResultats() {
            // Test de la commutativité
            assertEquals(calculator.add(5, 3), calculator.add(3, 5), "L'addition devrait être commutative");

            // Test de l'associativité
            assertEquals(calculator.add(calculator.add(2, 3), 4), calculator.add(2, calculator.add(3, 4)),
                "L'addition devrait être associative");
        }

        @Test
        @DisplayName("Test de performance (addition rapide)")
        void testPerformance() {
            long startTime = System.nanoTime();

            for (int i = 0; i < 1000000; i++) {
                calculator.add(i, i + 1);
            }

            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            // L'addition devrait être très rapide (< 1 seconde pour 1 million d'opérations)
            assertTrue(duration < 1_000_000_000L, "1 million d'additions devrait prendre moins d'1 seconde");
        }
    }
}