package io.github.katebulka.alteration;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.LongSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import io.github.katebulka.alteration.annotation.ConfigOption;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModConfigScreen {
	private static final Map<String, String> CATEGORY_KEYS = new LinkedHashMap<>() {{
		put("visual", "config.alteration.category.visual");
		put("audial", "config.alteration.category.audial");
	}};

	public static Screen createScreen(Screen parent) {
		YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder()
				.title(Component.translatable("config.alteration.title"))
				.save(ModConfig::save);

		Map<String, ConfigCategory.Builder> categories = new LinkedHashMap<>();
		CATEGORY_KEYS.forEach((id, key) -> categories.put(id, ConfigCategory.createBuilder().name(Component.translatable(key))));

		for (Field field : ModConfig.Keys.class.getDeclaredFields()) {
			ConfigOption ann = field.getAnnotation(ConfigOption.class);

			if (ann == null) continue;
			if (Modifier.isStatic(field.getModifiers())) continue;

			field.setAccessible(true);

			ConfigCategory.Builder cat = categories.get(ann.category());

			if (cat == null) {
				throw new IllegalStateException("");
			}

			cat.option(buildOption(field, ann));
		}

		categories.values().forEach(c -> builder.category(c.build()));
		return builder.build().generateScreen(parent);
	}

	private static Option<Boolean> buildBoolean(Field field, ConfigOption ann) {
		return Option.<Boolean>createBuilder()
				.name(name(ann)).description(desc(ann))
				.binding(
						defaultOf(field, Boolean.class),
						() -> (Boolean) get(field),
						v -> set(field, v))
				.controller(TickBoxControllerBuilder::create)
				.build();
	}

	private static Option<Float> buildFloat(Field field, ConfigOption ann) {
		return Option.<Float>createBuilder()
				.name(name(ann)).description(desc(ann))
				.binding(
						defaultOf(field, Float.class),
						() -> (Float) get(field),
						v -> set(field, v))
				.controller(o -> FloatFieldControllerBuilder.create(o)
						.min(ann.min()).max(ann.max()))
				.build();
	}

	private static ListOption<String> buildStringList(Field field, ConfigOption ann) {
		List<String> defaultList = (List<String>) defaultOf(field, List.class);

		return ListOption.<String>createBuilder().name(name(ann)).description(desc(ann))
				.binding(
						new ArrayList<>(defaultList),
						() -> new ArrayList<>((List<String>) get(field)),
						v -> set(field, new ArrayList<>(v)))
				.controller(StringControllerBuilder::create)
				.initial("").build();
	}

	private static Option<Integer> buildInteger(Field field, ConfigOption ann) {
		return Option.<Integer>createBuilder()
				.name(name(ann)).description(desc(ann))
				.binding(
						defaultOf(field, Integer.class),
						() -> (Integer) get(field),
						v -> set(field, v))
				.controller(o -> IntegerSliderControllerBuilder.create(o))
				.build();
	}

	private static Option<Double> buildDouble(Field field, ConfigOption ann) {
		return Option.<Double>createBuilder()
				.name(name(ann)).description(desc(ann))
				.binding(
						defaultOf(field, Double.class),
						() -> (Double) get(field),
						v -> set(field, v))
				.controller(o -> DoubleSliderControllerBuilder.create(o))
				.build();
	}

	private static Option<String> buildString(Field field, ConfigOption ann) {
		return Option.<String>createBuilder()
				.name(name(ann)).description(desc(ann))
				.binding(
						defaultOf(field, String.class),
						() -> (String) get(field),
						v -> set(field, v))
				.controller(o -> StringControllerBuilder.create(o))
				.build();
	}

	private static Option<Enum> buildEnum(Field field, ConfigOption ann) {
		return Option.<Enum>createBuilder()
				.name(name(ann)).description(desc(ann))
				.binding(
						defaultOf(field, Enum.class),
						() -> (Enum) get(field),
						v -> set(field, v))
				.controller(o -> EnumControllerBuilder.create(o))
				.build();
	}

	private static Option<?> buildOption(Field field, ConfigOption ann) {
		Class<?> t = field.getType();

		if (t == boolean.class) return buildBoolean(field, ann);
		if (t == float.class) return buildFloat(field, ann);
		if (t == int.class) return buildInteger(field, ann);
		if (t == double.class) return buildDouble(field, ann);
//		if (t == long.class) return buildLong(field, ann);
		if (t == String.class) return buildString(field, ann);
		if (t.isEnum()) return buildEnum(field, ann);

		// todo: i need to make proper list support, cuz right now it's mostly situational lol
		if (t == List.class) return buildStringList(field, ann);

		throw new IllegalStateException("");
	}

	private static Component name(ConfigOption ann) {
		return Component.translatable("config.alteration.option." + ann.key());
	}

	private static OptionDescription desc(ConfigOption ann) {
		return OptionDescription.of(Component.translatable("config.alteration.tooltip." + ann.key()));
	}

	private static Object get(Field field) {
		try {
			return field.get(ModConfig.KEYS);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static void set(Field field, Object value) {
		try {
			field.set(ModConfig.KEYS, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T defaultOf(Field field, Class<T> type) {
		try {
			return type.cast(field.get(new ModConfig.Keys()));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
