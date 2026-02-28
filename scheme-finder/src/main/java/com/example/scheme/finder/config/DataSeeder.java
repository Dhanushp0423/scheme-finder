package com.example.scheme.finder.config;

import com.example.scheme.finder.entity.Category;
import com.example.scheme.finder.entity.Scheme;
import com.example.scheme.finder.entity.User;
import com.example.scheme.finder.repository.CategoryRepository;
import com.example.scheme.finder.repository.SchemeRepository;
import com.example.scheme.finder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final SchemeRepository schemeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedCategories();
        seedAdminUser();
        seedSampleSchemes();
    }

    private void seedCategories() {
        if (categoryRepository.count() > 0) return;

        log.info("Seeding categories...");
        List<Category> categories = List.of(
                Category.builder().name("Agriculture, Rural & Environment").description("Schemes for farmers and rural development").iconUrl("🌾").colorCode("#16a34a").displayOrder(1).isActive(true).build(),
                Category.builder().name("Banking, Financial Services and Insurance").description("Financial assistance and insurance schemes").iconUrl("🏦").colorCode("#2563eb").displayOrder(2).isActive(true).build(),
                Category.builder().name("Business & Entrepreneurship").description("Business support and entrepreneurship schemes").iconUrl("💼").colorCode("#7c3aed").displayOrder(3).isActive(true).build(),
                Category.builder().name("Education & Learning").description("Educational scholarships and learning programs").iconUrl("📚").colorCode("#dc2626").displayOrder(4).isActive(true).build(),
                Category.builder().name("Health & Wellness").description("Healthcare and wellness schemes").iconUrl("🏥").colorCode("#db2777").displayOrder(5).isActive(true).build(),
                Category.builder().name("Housing & Shelter").description("Housing assistance and shelter schemes").iconUrl("🏠").colorCode("#ea580c").displayOrder(6).isActive(true).build(),
                Category.builder().name("Public Safety, Law & Justice").description("Safety and justice related schemes").iconUrl("⚖️").colorCode("#0891b2").displayOrder(7).isActive(true).build(),
                Category.builder().name("Science, IT & Communications").description("Technology and digital schemes").iconUrl("💻").colorCode("#0d9488").displayOrder(8).isActive(true).build(),
                Category.builder().name("Skills & Employment").description("Skill development and employment schemes").iconUrl("🛠️").colorCode("#ca8a04").displayOrder(9).isActive(true).build(),
                Category.builder().name("Social Welfare & Empowerment").description("Social welfare for marginalized sections").iconUrl("🤝").colorCode("#9333ea").displayOrder(10).isActive(true).build(),
                Category.builder().name("Sports & Culture").description("Sports and cultural promotion schemes").iconUrl("🏆").colorCode("#059669").displayOrder(11).isActive(true).build(),
                Category.builder().name("Transport & Infrastructure").description("Transport and infrastructure schemes").iconUrl("🚗").colorCode("#475569").displayOrder(12).isActive(true).build(),
                Category.builder().name("Travel & Tourism").description("Travel and tourism promotion schemes").iconUrl("✈️").colorCode("#0284c7").displayOrder(13).isActive(true).build(),
                Category.builder().name("Utility & Sanitation").description("Water, electricity and sanitation schemes").iconUrl("💧").colorCode("#0369a1").displayOrder(14).isActive(true).build(),
                Category.builder().name("Women and Child").description("Schemes for women and child welfare").iconUrl("👩‍👧").colorCode("#be185d").displayOrder(15).isActive(true).build()
        );

        categoryRepository.saveAll(categories);
        log.info("Seeded {} categories", categories.size());
    }

    private void seedAdminUser() {
        if (userRepository.existsByEmail("dhanushp0408@gmail.com")) return;

        log.info("Seeding admin user...");
        User admin = User.builder()
                .fullName("System Administrator")
                .email("dhanushp0408@gmail.com")
                .password(passwordEncoder.encode("DHANUSH@0423"))
                .role(User.Role.ADMIN)
                .isActive(true)
                .emailVerified(true)
                .isDisabled(false)
                .isStudent(false)
                .isEmployed(true)
                .build();
        userRepository.save(admin);
        log.info("Admin user created successfully");
    }

    private void seedSampleSchemes() {
        if (schemeRepository.count() > 0) return;

        log.info("Seeding sample schemes...");
        Category educationCat = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream().filter(c -> c.getName().contains("Education")).findFirst().orElse(null);
        Category healthCat = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream().filter(c -> c.getName().contains("Health")).findFirst().orElse(null);
        Category agriCat = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream().filter(c -> c.getName().contains("Agriculture")).findFirst().orElse(null);

        List<Scheme> schemes = List.of(
                Scheme.builder()
                        .title("PM Scholarship Scheme")
                        .shortDescription("Scholarship for higher technical education for wards of Central Armed Police Forces and Railway Protection Force personnel.")
                        .fullDescription("The Prime Minister's Scholarship Scheme (PMSS) was launched in 2006-07 for the wards and widows of Ex-servicemen/Ex-Coast Guard personnel. The scheme provides scholarship for higher technical and professional education. The scholarship is awarded to students pursuing professional degree courses such as BE, B.Tech, BDS, MBBS, B.Ed, BBA, BCA, B.Sc (Nursing/Agriculture), and equivalent technical/professional degree and diploma courses.")
                        .category(educationCat)
                        .ministryName("Ministry of Home Affairs")
                        .schemeLevel(Scheme.SchemeLevel.CENTRAL)
                        .status(Scheme.SchemeStatus.ACTIVE)
                        .genderEligibility(Scheme.GenderEligibility.ALL)
                        .minAge(18)
                        .maxAge(25)
                        .benefitType("Financial")
                        .benefitAmount("₹2,500 - ₹3,000 per month")
                        .benefits("Monthly scholarship amount for 5 years of study")
                        .isForStudents(true)
                        .isForDisabled(false)
                        .officialWebsite("https://scholarships.gov.in")
                        .applicationUrl("https://scholarships.gov.in")
                        .tags("scholarship,education,students,central")
                        .isFeatured(true)
                        .viewCount(1250L)
                        .build(),

                Scheme.builder()
                        .title("Ayushman Bharat - Pradhan Mantri Jan Arogya Yojana (PM-JAY)")
                        .shortDescription("Health cover of ₹5 lakh per family per year for secondary and tertiary care hospitalization.")
                        .fullDescription("Ayushman Bharat PM-JAY is the world's largest health insurance/assurance scheme fully financed by the government. It provides a health cover of Rs. 5 lakhs per family per year for secondary and tertiary care hospitalization to over 12 crore poor and vulnerable families (approximately 55 crore beneficiaries). These families constituted the bottom 40% of the Indian population.")
                        .category(healthCat)
                        .ministryName("Ministry of Health and Family Welfare")
                        .schemeLevel(Scheme.SchemeLevel.CENTRAL)
                        .status(Scheme.SchemeStatus.ACTIVE)
                        .genderEligibility(Scheme.GenderEligibility.ALL)
                        .maxAnnualIncome(250000.0)
                        .benefitType("Non-Financial")
                        .benefitAmount("₹5 Lakh health coverage per family/year")
                        .benefits("Free hospitalization up to 5 lakh per year, covers 1350+ medical packages")
                        .eligibleCategories("General,OBC,SC,ST")
                        .officialWebsite("https://pmjay.gov.in")
                        .applicationUrl("https://pmjay.gov.in")
                        .tags("health,insurance,ayushman,hospitalization,central")
                        .isFeatured(true)
                        .viewCount(3500L)
                        .build(),

                Scheme.builder()
                        .title("PM Kisan Samman Nidhi")
                        .shortDescription("Income support of ₹6000 per year to all land holding farmer families across the country.")
                        .fullDescription("PM-KISAN is a Central Sector scheme with 100% funding from Government of India. Under the scheme an income support of 6,000/- per year in three equal installments will be provided to all land holding farmer families, subject to certain exclusion criteria relating to higher income status.")
                        .category(agriCat)
                        .ministryName("Ministry of Agriculture and Farmers Welfare")
                        .schemeLevel(Scheme.SchemeLevel.CENTRAL)
                        .status(Scheme.SchemeStatus.ACTIVE)
                        .genderEligibility(Scheme.GenderEligibility.ALL)
                        .benefitType("Financial")
                        .benefitAmount("₹6,000 per year (₹2,000 every 4 months)")
                        .benefits("Direct income support of Rs 6000 annually in 3 installments")
                        .tags("agriculture,farmers,income support,PM Kisan,central")
                        .officialWebsite("https://pmkisan.gov.in")
                        .applicationUrl("https://pmkisan.gov.in")
                        .isFeatured(true)
                        .viewCount(5200L)
                        .build()
        );

        schemeRepository.saveAll(schemes);
        log.info("Seeded {} sample schemes", schemes.size());
    }
}
