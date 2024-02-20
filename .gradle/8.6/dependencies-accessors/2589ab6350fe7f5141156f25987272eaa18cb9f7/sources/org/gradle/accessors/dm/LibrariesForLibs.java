package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AlchemistLibraryAccessors laccForAlchemistLibraryAccessors = new AlchemistLibraryAccessors(owner);
    private final CollektiveLibraryAccessors laccForCollektiveLibraryAccessors = new CollektiveLibraryAccessors(owner);
    private final ProtelisLibraryAccessors laccForProtelisLibraryAccessors = new ProtelisLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Dependency provider for <b>caffeine</b> with <b>com.github.ben-manes.caffeine:caffeine</b> coordinates and
     * with version <b>3.1.8</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getCaffeine() {
        return create("caffeine");
    }

    /**
     * Dependency provider for <b>scala</b> with <b>org.scala-lang:scala-library</b> coordinates and
     * with version reference <b>scala</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getScala() {
        return create("scala");
    }

    /**
     * Group of libraries at <b>alchemist</b>
     */
    public AlchemistLibraryAccessors getAlchemist() {
        return laccForAlchemistLibraryAccessors;
    }

    /**
     * Group of libraries at <b>collektive</b>
     */
    public CollektiveLibraryAccessors getCollektive() {
        return laccForCollektiveLibraryAccessors;
    }

    /**
     * Group of libraries at <b>protelis</b>
     */
    public ProtelisLibraryAccessors getProtelis() {
        return laccForProtelisLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class AlchemistLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final AlchemistEuclideanLibraryAccessors laccForAlchemistEuclideanLibraryAccessors = new AlchemistEuclideanLibraryAccessors(owner);
        private final AlchemistIncarnationLibraryAccessors laccForAlchemistIncarnationLibraryAccessors = new AlchemistIncarnationLibraryAccessors(owner);

        public AlchemistLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>alchemist</b> with <b>it.unibo.alchemist:alchemist</b> coordinates and
         * with version reference <b>alchemist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("alchemist");
        }

        /**
         * Dependency provider for <b>swingui</b> with <b>it.unibo.alchemist:alchemist-swingui</b> coordinates and
         * with version reference <b>alchemist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSwingui() {
            return create("alchemist.swingui");
        }

        /**
         * Dependency provider for <b>test</b> with <b>it.unibo.alchemist:alchemist-test</b> coordinates and
         * with version reference <b>alchemist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTest() {
            return create("alchemist.test");
        }

        /**
         * Group of libraries at <b>alchemist.euclidean</b>
         */
        public AlchemistEuclideanLibraryAccessors getEuclidean() {
            return laccForAlchemistEuclideanLibraryAccessors;
        }

        /**
         * Group of libraries at <b>alchemist.incarnation</b>
         */
        public AlchemistIncarnationLibraryAccessors getIncarnation() {
            return laccForAlchemistIncarnationLibraryAccessors;
        }

    }

    public static class AlchemistEuclideanLibraryAccessors extends SubDependencyFactory {

        public AlchemistEuclideanLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>geometry</b> with <b>it.unibo.alchemist:alchemist-euclidean-geometry</b> coordinates and
         * with version reference <b>alchemist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGeometry() {
            return create("alchemist.euclidean.geometry");
        }

    }

    public static class AlchemistIncarnationLibraryAccessors extends SubDependencyFactory {

        public AlchemistIncarnationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>protelis</b> with <b>it.unibo.alchemist:alchemist-incarnation-protelis</b> coordinates and
         * with version reference <b>alchemist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getProtelis() {
            return create("alchemist.incarnation.protelis");
        }

        /**
         * Dependency provider for <b>scafi</b> with <b>it.unibo.alchemist:alchemist-incarnation-scafi</b> coordinates and
         * with version reference <b>alchemist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getScafi() {
            return create("alchemist.incarnation.scafi");
        }

    }

    public static class CollektiveLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public CollektiveLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>collektive</b> with <b>it.unibo.collektive:dsl</b> coordinates and
         * with version reference <b>collektive</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("collektive");
        }

        /**
         * Dependency provider for <b>incarnation</b> with <b>it.unibo.collektive:alchemist-incarnation-collektive</b> coordinates and
         * with version <b>7.0.3</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getIncarnation() {
            return create("collektive.incarnation");
        }

    }

    public static class ProtelisLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public ProtelisLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>protelis</b> with <b>org.protelis:protelis</b> coordinates and
         * with version reference <b>protelis</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("protelis");
        }

        /**
         * Dependency provider for <b>interpreter</b> with <b>org.protelis:protelis-interpreter</b> coordinates and
         * with version reference <b>protelis</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getInterpreter() {
            return create("protelis.interpreter");
        }

        /**
         * Dependency provider for <b>lang</b> with <b>org.protelis:protelis-lang</b> coordinates and
         * with version reference <b>protelis</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLang() {
            return create("protelis.lang");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>alchemist</b> with value <b>30.1.11</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAlchemist() { return getVersion("alchemist"); }

        /**
         * Version alias <b>collektive</b> with value <b>7.0.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCollektive() { return getVersion("collektive"); }

        /**
         * Version alias <b>kotlin</b> with value <b>1.9.22</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getKotlin() { return getVersion("kotlin"); }

        /**
         * Version alias <b>protelis</b> with value <b>17.3.16</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getProtelis() { return getVersion("protelis"); }

        /**
         * Version alias <b>scala</b> with value <b>2.13.12</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getScala() { return getVersion("scala"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Dependency bundle provider for <b>alchemist</b> which contains the following dependencies:
         * <ul>
         *    <li>it.unibo.alchemist:alchemist</li>
         *    <li>it.unibo.alchemist:alchemist-euclidean-geometry</li>
         *    <li>it.unibo.alchemist:alchemist-incarnation-protelis</li>
         *    <li>it.unibo.alchemist:alchemist-incarnation-scafi</li>
         *    <li>it.unibo.alchemist:alchemist-swingui</li>
         *    <li>it.unibo.alchemist:alchemist-test</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getAlchemist() {
            return createBundle("alchemist");
        }

        /**
         * Dependency bundle provider for <b>collektive</b> which contains the following dependencies:
         * <ul>
         *    <li>it.unibo.collektive:dsl</li>
         *    <li>it.unibo.collektive:alchemist-incarnation-collektive</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getCollektive() {
            return createBundle("collektive");
        }

        /**
         * Dependency bundle provider for <b>protelis</b> which contains the following dependencies:
         * <ul>
         *    <li>org.protelis:protelis</li>
         *    <li>org.protelis:protelis-interpreter</li>
         *    <li>org.protelis:protelis-lang</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getProtelis() {
            return createBundle("protelis");
        }

    }

    public static class PluginAccessors extends PluginFactory {
        private final KotlinPluginAccessors paccForKotlinPluginAccessors = new KotlinPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>collektive</b> with plugin id <b>it.unibo.collektive.collektive-plugin</b> and
         * with version reference <b>collektive</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getCollektive() { return createPlugin("collektive"); }

        /**
         * Plugin provider for <b>multiJvmTesting</b> with plugin id <b>org.danilopianini.multi-jvm-test-plugin</b> and
         * with version <b>0.5.8</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getMultiJvmTesting() { return createPlugin("multiJvmTesting"); }

        /**
         * Plugin provider for <b>taskTree</b> with plugin id <b>com.dorongold.task-tree</b> and
         * with version <b>2.1.1</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getTaskTree() { return createPlugin("taskTree"); }

        /**
         * Group of plugins at <b>plugins.kotlin</b>
         */
        public KotlinPluginAccessors getKotlin() {
            return paccForKotlinPluginAccessors;
        }

    }

    public static class KotlinPluginAccessors extends PluginFactory {

        public KotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>kotlin.jvm</b> with plugin id <b>org.jetbrains.kotlin.jvm</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getJvm() { return createPlugin("kotlin.jvm"); }

    }

}
