package ru.petstore.sandbox.tests;

import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestBase {
    protected static final Logger LOG = LogManager.getLogger(Logger.class);

    protected static final Faker random = new Faker();

}