package com.example.model

data class TeachingStrategy(
    val id: String,
    val titleKhmer: String,
    val subtitleKhmer: String,
    val descriptionKhmer: String,
    val stepsKhmer: List<String>,
    val tipsForTeacherKhmer: String
)

data class CommonMisconception(
    val topicKhmer: String,
    val wrongIdeaKhmer: String,
    val correctIdeaKhmer: String,
    val pedagogicalSolutionKhmer: String
)

object TeachingHandbookData {

    val strategies: List<TeachingStrategy> = listOf(
        TeachingStrategy(
            id = "cpa",
            titleKhmer = "ទ្រឹស្តីបង្រៀន រូបី - ពាក់កណ្តាលរូបី - អរូបី (CPA)",
            subtitleKhmer = "មូលដ្ឋានគ្រឹះគរុកោសល្យគណិតវិទ្យាបឋមសិក្សា",
            descriptionKhmer = "ការបង្រៀនគណិតវិទ្យាថ្នាក់ទី៣ ត្រូវផ្តើមចេញពីវត្ថុជាក់ស្តែង (រូបី) ឈានទៅរកគំនូសតំណាង/ដ្យាក្រាម (ពាក់កណ្តាលរូបី) និងឈានដល់និមិត្តសញ្ញាលេខ (អរូបី) ដើម្បីឱ្យសិស្សកសាងបញ្ញត្តិដោយខ្លួនឯង។",
            stepsKhmer = listOf(
                "ដំណាក់កាលរូបី (Concrete)៖ ប្រើប្រាស់បំពង់បឺត, ក្រដាសប្រាក់, ថាសចំនួន, បន្ទះដប់, បន្ទះរយ និងដុំពាន់ជាក់ស្តែង។",
                "ដំណាក់កាលពាក់កណ្តាលរូបី (Pictorial)៖ គូរដ្យាក្រាមរនាំង (Bar Model), បន្ទាត់គ្មានលេខ (Empty Number Line), រូបតំណាងចំណែក។",
                "ដំណាក់កាលអរូបី (Abstract)៖ សរសេរប្រមាណវិធីតាមជួរឈរ ជួរដេក និងនិមិត្តសញ្ញាគណិតវិទ្យា (+, -, ×, ÷, <, >, =)."
            ),
            tipsForTeacherKhmer = "កុំប្រញាប់ប្រញាល់ឱ្យសិស្សទន្ទេញរូបមន្តអរូបី មុនពេលពួកគេបានកាន់ និងរៀបចំសម្ភាររូបីដោយដៃផ្ទាល់។"
        ),
        TeachingStrategy(
            id = "polya",
            titleKhmer = "វិធីសាស្ត្រដោះស្រាយចំណោទ ៤ ជំហាន (Polya)",
            subtitleKhmer = "បណ្តុះការគិតស៊ីជម្រៅក្នុងការដោះស្រាយបញ្ហាជីវភាព",
            descriptionKhmer = "យុទ្ធវិធីសកលរបស់លោក Polya ជួយឱ្យសិស្សថ្នាក់ទី៣ មិនខ្លាចចំណោទ និងចេះវិភាគបញ្ហាជាជំហានៗ។",
            stepsKhmer = listOf(
                "ជំហានទី១៖ ការយល់ដឹងពីបញ្ហា (Understanding the problem) - អានប្រធាន ២-៣ដង, កំណត់ប្រាប់អ្វីខ្លះ, សួររកអ្វីខ្លះ។",
                "ជំហានទី២៖ ការបង្កើតផែនការ (Devising a plan) - គូសដ្យាក្រាមតំណាង, ជ្រើសរើសប្រមាណវិធី (បូក ដក គុណ ឬចែក), រៀបចំល្បះលេខ។",
                "ជំហានទី៣៖ ការអនុវត្តផែនការ (Implementing the plan) - គណនាប្រមាណវិធីតាមជួរឈរ, សរសេររបៀបឆ្លើយ និងខ្នាត។",
                "ជំហានទី៤៖ ការពិនិត្យឡើងវិញ (Reflecting/Verifying) - ផ្ទៀងផ្ទាត់ចម្លើយ (ឧ. ផលដក + តួដក = តំណាងដក) និងឆ្លុះបញ្ចាំងភាពសមហេតុផល។"
            ),
            tipsForTeacherKhmer = "លើកទឹកចិត្តឱ្យសិស្សគូសដ្យាក្រាមរនាំង (Bar Model) ដើម្បីមើលឃើញទំនាក់ទំនងរវាងផ្នែកតូច និងផ្នែកសរុប។"
        ),
        TeachingStrategy(
            id = "empty_line",
            titleKhmer = "វិធីគណនាតាមបន្ទាត់គ្មានលេខ (Empty Number Line)",
            subtitleKhmer = "ជំនួយដល់មុធាគណនា (Mental Math)",
            descriptionKhmer = "ការគណនាដោយលោតលើបន្ទាត់ចំនួនជួយឱ្យសិស្សយល់ពីចលនានៃចំនួន និងបំបែកចំនួនតាមគោលដប់ និងគោលរយ។",
            stepsKhmer = listOf(
                "Jump (លោតធម្មតា)៖ ឧទាហរណ៍ 530 - 110 = ? -> លោតថយ 100 បាន 430 រួចលោតថយ 10 បាន 420។",
                "Over-jump (លោតលើសហើយត្រឡប់)៖ លោតលើសទៅចំនួនគត់មូល រួចលោតថយមកវិញ។",
                "Split-jump (លោតបំបែក)៖ បំបែកតួទី២ ជាទម្រង់ពង្រាយ រួចលោតតាមខ្ទង់នីមួយៗ។"
            ),
            tipsForTeacherKhmer = "វិធីនេះល្អឥតខ្ចោះសម្រាប់ជួយសិស្សដែលពិបាកធ្វើប្រមាណវិធីតាមជួរឈរដែលមានខ្ចីច្រើនដង។"
        ),
        TeachingStrategy(
            id = "multiplication_table",
            titleKhmer = "ការបង្កើតតារាងមេគុណ និងលក្ខណៈត្រឡប់",
            subtitleKhmer = "ពីការបូកដដែលៗ ទៅជាផលគុណ",
            descriptionKhmer = "សិស្សយល់ថា វិធីគុណ គឺជាការបូកនៃតួបូកដដែលៗច្រើនដង។ លក្ខណៈត្រឡប់ a × b = b × a ជួយកាត់បន្ថយការចងចាំពាក់កណ្តាល។",
            stepsKhmer = listOf(
                "រាប់បន្ថែមស្មើៗគ្នា៖ រាប់បន្ថែមម្តង ២, ៣, ៤, ៥, ៦, ៧, ៨, ៩, ១០។",
                "បំប្លែងពីបូកទៅគុណ៖ ៧ + ៧ + ៧ + ៧ = ៧ × ៤ = ២៨។",
                "អនុវត្តលក្ខណៈត្រឡប់៖ ៧ × ៤ = ៤ × ៧ = ២៨។ ជួរដេក និងជួរឈរនៃក្រឡាចតុកោណកែងស្មើគ្នា។"
            ),
            tipsForTeacherKhmer = "រៀបចំល្បែងសិក្សា «ផ្គូផ្គងចម្លើយ» ឬ «ប័ណ្ណសញ្ញាចរាចរណ៍» ដើម្បីឱ្យសិស្សចូលរួមយ៉ាងសកម្ម។"
        )
    )

    val misconceptions: List<CommonMisconception> = listOf(
        CommonMisconception(
            topicKhmer = "ការដកមានខ្ចី (Subtraction with Borrowing)",
            wrongIdeaKhmer = "សិស្សច្រើនយកលេខធំដកលេខតូចដោយមិនខ្វល់ថាវាជាតំណាងដក ឬតួដក (ឧ. ៤២ - ២៧ ស្រាប់តែយក ៧ - ២ = ៥ បានចម្លើយ ២៥)។",
            correctIdeaKhmer = "ខ្ទង់រាយ ២ មិនអាចដក ៧ បានទេ ត្រូវខ្ចី ១ដប់ ពីខ្ទង់ដប់ ធ្វើឱ្យ ២ ក្លាយជា ១២ រួចទើបដក ៧ សល់ ៥។ ខ្ទង់ដប់ ៤ សល់ ៣ ដក ២ សល់ ១ (ចម្លើយ ១៥)។",
            pedagogicalSolutionKhmer = "ប្រើបន្ទះដប់ និងបន្ទះរាយជាក់ស្តែងឱ្យសិស្សស្រាយបាច់ ១ដប់ ទៅជា ១០រាយ ដើម្បីឱ្យឃើញច្បាស់ពីការខ្ចី។"
        ),
        CommonMisconception(
            topicKhmer = "ការយល់ដឹងពីលេខសូន្យក្នុងប្រមាណវិធី (Zero in Operations)",
            wrongIdeaKhmer = "គិតថាចំនួនបូកនឹង ០ ស្មើ ០ ឬ គុណនឹង ០ ស្មើចំនួនដើម។",
            correctIdeaKhmer = "ចំនួនគត់ណាក៏ដោយ បូកនឹង ០ ស្មើចំនួននោះដដែល (a + 0 = a)។ ចំនួនណាក៏ដោយគុណនឹង ០ គឺស្មើ ០ (a × 0 = 0)។",
            pedagogicalSolutionKhmer = "លើកឧទាហរណ៍ជាក់ស្តែង៖ មានផ្លែក្រូច ៥ផ្លែ ក្នុងកន្ត្រក មិនថែមអ្វីសោះ (ថែម ០) នៅតែមាន ៥ផ្លែ។ កន្ត្រកទទេ ៥កន្ត្រក (៥ × ០) គឺមានផ្លែក្រូច ០ផ្លែ។"
        ),
        CommonMisconception(
            topicKhmer = "ការប្រៀបធៀបប្រភាគ (Fraction Comparison)",
            wrongIdeaKhmer = "សិស្សគិតថា ភាគបែងកាន់តែធំ ប្រភាគកាន់តែធំ (ឧ. គិតថា 1/8 ធំជាង 1/2 ព្រោះ 8 ធំជាង 2)។",
            correctIdeaKhmer = "សម្រាប់ភាគយកស្មើគ្នា ភាគបែងកាន់តែធំ តម្លៃនៃចំណែកកាន់តែតូច (1/2 ធំជាង 1/8 ព្រោះនំពុះជា ២ចំណែកធំជាងនំពុះជា ៨ចំណែក)។",
            pedagogicalSolutionKhmer = "បង្ហាញជារូបភាពនំ ឬបន្ទះប្រភាគជាក់ស្តែងដើម្បីប្រៀបធៀបទំហំចំណែកនីមួយៗ។"
        )
    )
}
