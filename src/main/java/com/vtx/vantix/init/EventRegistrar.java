package com.vtx.vantix.init;

import com.vtx.vantix.Vantix;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Auto-discovers all classes in the {@code com.vtx.vantix} package at mod
 * initialization and registers them based on annotations:
 * <ul>
 *   <li>{@link RegisterEvents {@code @RegisterEvents}} → {@link
 *       MinecraftForge#EVENT_BUS}</li>
 *   <li>{@link RegisterCommand {@code @RegisterCommand}} → {@link
 *       ClientCommandHandler}</li>
 *   <li>{@link RegisterInstance {@code @RegisterInstance}} → command or event bus
 *       depending on field type</li>
 *   <li>{@link RegisterKeybind {@code @RegisterKeybind}} → {@link
 *       ClientRegistry#registerKeyBinding}</li>
 * </ul>
 */
public class EventRegistrar {

    private static final String BASE_PACKAGE = "com.vtx.vantix";

    /**
     * Scans the classpath, loads every discovered class, and attempts each of the
     * four registration paths on it.
     */
    public static void registerAll() {
        List<String> classNames = ClasspathScanner.findClassNames(EventRegistrar.class, BASE_PACKAGE);
        List<Class<?>> classes = ClasspathScanner.loadClasses(classNames, EventRegistrar.class.getClassLoader());
        for (Class<?> clazz : classes) {
            tryRegisterEvents(clazz);
            tryRegisterCommand(clazz);
            tryRegisterInstanceFields(clazz);
            tryRegisterKeybindFields(clazz);
        }
    }

    /**
     * If {@code clazz} is annotated with {@code @RegisterEvents}, instantiate it
     * and subscribe it to {@link MinecraftForge#EVENT_BUS}.
     */
    private static void tryRegisterEvents(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(RegisterEvents.class)) return;
        try {
            MinecraftForge.EVENT_BUS.register(newInstance(clazz));
        } catch (Throwable t) {
            Vantix.logger.severe("[VNTX] Failed to register events for " + clazz.getName() + ": " + t.getMessage());
        }
    }

    /**
     * If {@code clazz} is annotated with {@code @RegisterCommand} and implements
     * {@link ICommand}, instantiate and register it with
     * {@link ClientCommandHandler}.
     */
    private static void tryRegisterCommand(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(RegisterCommand.class)) return;
        if (!ICommand.class.isAssignableFrom(clazz)) {
            Vantix.logger.severe("[VNTX] @RegisterCommand class does not implement ICommand: " + clazz.getName());
            return;
        }
        try {
            ClientCommandHandler.instance.registerCommand((ICommand) newInstance(clazz));
        } catch (Throwable t) {
            Vantix.logger.severe("[VNTX] Failed to register command: " + clazz.getName() + ": " + t.getMessage());
        }
    }

    /**
     * For every {@code @RegisterInstance} static field in {@code clazz}, read the
     * field value and register it, as a command if it implements {@link ICommand},
     * otherwise as an event-bus listener.
     */
    private static void tryRegisterInstanceFields(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(RegisterInstance.class)) continue;
            if (!Modifier.isStatic(field.getModifiers())) {
                Vantix.logger.severe("[VNTX] @RegisterInstance field must be static: " + clazz.getName() + "." + field.getName());
                continue;
            }
            try {
                field.setAccessible(true);
                Object instance = field.get(null);
                if (instance == null) {
                    Vantix.logger.severe("[VNTX] @RegisterInstance field is null: " + field.getName());
                    continue;
                }
                if (instance instanceof ICommand) {
                    ClientCommandHandler.instance.registerCommand((ICommand) instance);
                } else {
                    MinecraftForge.EVENT_BUS.register(instance);
                }
            } catch (Throwable t) {
                Vantix.logger.severe("[VNTX] Failed to register instance field: " + field.getName() + ": " + t.getMessage());
            }
        }
    }

    /**
     * For every {@code @RegisterKeybind} static {@link KeyBinding} field in
     * {@code clazz}, read the field value and register it with
     * {@link ClientRegistry#registerKeyBinding}.
     */
    private static void tryRegisterKeybindFields(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(RegisterKeybind.class)) continue;
            if (!Modifier.isStatic(field.getModifiers())) {
                Vantix.logger.severe("[VNTX] @RegisterKeybind field must be static: " + clazz.getName() + "." + field.getName());
                continue;
            }
            if (!KeyBinding.class.isAssignableFrom(field.getType())) {
                Vantix.logger.severe("[VNTX] @RegisterKeybind field is not a KeyBinding: " + field.getName());
                continue;
            }
            try {
                field.setAccessible(true);
                KeyBinding key = (KeyBinding) field.get(null);
                if (key == null) {
                    Vantix.logger.severe("[VNTX] @RegisterKeybind field is null: " + field.getName());
                    continue;
                }
                ClientRegistry.registerKeyBinding(key);
            } catch (Throwable t) {
                Vantix.logger.severe("[VNTX] Failed to register keybind: " + field.getName() + ": " + t.getMessage());
            }
        }
    }

    private static Object newInstance(Class<?> clazz) throws Exception {
        Constructor<?> c = clazz.getDeclaredConstructor();
        c.setAccessible(true);
        return c.newInstance();
    }
}