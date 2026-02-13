# Getting Started with Včelárska Aplikácia

## 🎉 Project Successfully Created!

Your Android beekeeping application has been initialized with a complete foundation. Here's everything you need to know to continue development.

## 📊 What Has Been Implemented

### Phase 1: Foundation & Setup ✅ COMPLETE

**Files Created:** 63 files
- **37 Java classes** (entities, DAOs, repositories, base classes, fragments)
- **20 XML files** (layouts, navigation, resources)
- **6 configuration files** (Gradle, manifest, etc.)

**Database:** Fully designed and implemented
- 9 Room entities with relationships
- 9 DAO interfaces with RxJava2 queries
- Complete schema for all beekeeping data

**Architecture:** MVVM pattern ready
- Base classes for Activity, Fragment, ViewModel
- Repository pattern for data access
- Navigation Component with drawer menu

**UI:** Material Design skeleton
- Main activity with navigation drawer
- 6 placeholder fragments ready for implementation
- Bee-themed color scheme (yellow/amber)

## 🚀 How to Open and Build

### Step 1: Open in Android Studio
```bash
# From Android Studio:
File → Open → Navigate to: /Users/juraj.kral/IdeaProjects/LearningProject
```

### Step 2: Sync Gradle
- Android Studio will prompt to sync Gradle
- Click "Sync Now" or File → Sync Project with Gradle Files
- Wait for dependencies to download (may take a few minutes)

### Step 3: Build the Project
- Build → Make Project (Cmd+F9)
- Check for any errors in the Build tab

### Step 4: Run on Device/Emulator
- Run → Run 'app' (Ctrl+R)
- Select a device or create an emulator (API 26+)
- App will install and launch with navigation drawer

## 📱 What You'll See

When you run the app:
1. **Launch screen** - Bee-themed yellow/amber colors
2. **Hamburger menu** (☰) in top-left
3. **Navigation drawer** with 6 sections:
   - Prehľad (Dashboard)
   - Včelnice (Apiaries)
   - Kalendár (Calendar)
   - Kalkulačky (Calculators)
   - Analýza (Analytics)
   - Nastavenia (Settings)
4. Each section shows "Coming Soon" placeholder

## 🛠️ Next Steps - Phase 2 Implementation

### Priority 1: Apiary Management (Včelnice)
**File:** `ApiaryListFragment.java`
**Tasks:**
1. Create `ApiaryViewModel.java` with LiveData
2. Create RecyclerView adapter for apiary list
3. Implement "Add Apiary" FAB (Floating Action Button)
4. Create dialog for apiary CRUD (add/edit)
5. Wire up to ApiaryRepository

**Example code structure:**
```java
// ApiaryViewModel.java
public class ApiaryViewModel extends BaseViewModel {
    private ApiaryRepository repository;
    private MutableLiveData<List<Apiary>> apiaries = new MutableLiveData<>();

    public void loadApiaries() {
        addDisposable(repository.getAllApiaries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(apiaries::setValue));
    }
}
```

### Priority 2: Hive Management (Úle)
**Files:** Create new HiveListFragment, HiveDetailFragment
**Tasks:**
1. Navigate from Apiary → Hive list
2. Display hives by apiary
3. Show hive details (tabs: inspections, feeding, taxation)
4. CRUD operations for hives

### Priority 3: Manual Inspection Entry
**Files:** Create InspectionEntryFragment
**Tasks:**
1. Form with all fields (date, temperature, strength, etc.)
2. Save inspection via InspectionRepository
3. Display in hive detail

### Priority 4: Testing
1. Unit tests for repositories
2. Test database operations
3. UI navigation tests

## 📚 Important Files Reference

| Task | File Location |
|------|--------------|
| Database schema | `app/src/main/java/com/beekeeper/app/data/local/entity/` |
| Add new DAO method | `app/src/main/java/com/beekeeper/app/data/local/dao/` |
| Create new repository | `app/src/main/java/com/beekeeper/app/data/repository/` |
| Add UI screen | `app/src/main/java/com/beekeeper/app/presentation/` |
| Edit strings | `app/src/main/res/values/strings.xml` |
| Add navigation | `app/src/main/res/navigation/nav_graph.xml` |
| Add dependencies | `app/build.gradle` |

## 🔧 Common Tasks

### Add a new dependency
Edit `app/build.gradle`, add to dependencies block:
```gradle
dependencies {
    implementation 'com.example:library:1.0.0'
}
```
Then sync Gradle.

### Create a new Fragment
1. Right-click package → New → Java Class
2. Extend `BaseFragment<YourBinding>`
3. Implement required methods
4. Add to navigation graph
5. Create corresponding XML layout

### Add new database table
1. Create Entity class in `entity/` package
2. Create DAO interface in `dao/` package
3. Add to AppDatabase @Database annotation
4. Increment DATABASE_VERSION in Constants
5. Create Repository class

### Add string resource
Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="your_key">Your Slovak Text</string>
```

## 🐛 Troubleshooting

### Gradle sync fails
- Check internet connection (needs to download dependencies)
- File → Invalidate Caches / Restart
- Delete `.gradle` folder and sync again

### Build errors
- Make sure JDK 8+ is configured
- Build → Clean Project, then Build → Rebuild Project
- Check Android SDK is installed (API 34)

### App crashes on launch
- Check Logcat for error messages
- Verify AndroidManifest.xml has MainActivity
- Ensure all fragments are in correct package

### Room database errors
- Increment DATABASE_VERSION if schema changed
- Delete app data and reinstall
- Check @Entity and @DAO annotations

## 📖 Learning Resources

### Android Development
- Official Android documentation: https://developer.android.com
- Room database guide: https://developer.android.com/training/data-storage/room
- Navigation component: https://developer.android.com/guide/navigation

### RxJava2
- RxJava tutorial: https://www.vogella.com/tutorials/RxJava/article.html
- Operators: https://reactivex.io/documentation/operators.html

### Material Design
- Material Design guidelines: https://material.io/design
- Android Material Components: https://material.io/develop/android

## 🎯 Recommended Development Order

1. **Week 1-2:** Implement Apiary CRUD
   - ViewModels, adapters, dialogs
   - Test adding/editing/deleting apiaries

2. **Week 3-4:** Implement Hive CRUD
   - Hive list by apiary
   - Hive detail screen with navigation

3. **Week 5-6:** Manual Inspection Entry
   - Form design and validation
   - Save to database
   - Display inspection history

4. **Week 7-8:** Feeding and Taxation Entry
   - Similar forms to inspection
   - Weight tracking for feeding
   - Frame-by-frame taxation input

5. **Week 9-10:** Calendar and Events
   - Calendar view
   - Add/edit events
   - Notifications

6. **Week 11-12:** Calculators
   - Varroa growth calculator
   - Queen rearing timeline

7. **Week 13-15:** OpenAI Integration
   - Audio recording service
   - Whisper API integration
   - GPT data extraction

8. **Week 16-18:** Excel Import
   - Apache POI implementation
   - Parse all sheets
   - Import historical data

9. **Week 19-20:** Analytics
   - Charts with MPAndroidChart
   - Trend analysis
   - Data export

10. **Week 21+:** Polish and Cloud Sync
    - UI improvements
    - Testing
    - Backend API
    - Synchronization

## 💡 Tips for Success

1. **Start small:** Implement one feature at a time
2. **Test frequently:** Run the app after each change
3. **Use Logcat:** Essential for debugging (View → Tool Windows → Logcat)
4. **Commit often:** Use Git to track your progress
5. **Follow patterns:** Use existing code as templates
6. **Don't rush:** Quality over speed

## 🆘 Need Help?

### Common questions:
- **Q: Where do I add a new screen?**
  - A: Create Fragment in presentation package, add to nav_graph.xml

- **Q: How do I access database?**
  - A: Use Repository classes, never access DAO directly from UI

- **Q: Where are strings stored?**
  - A: `res/values/strings.xml` - always use string resources, not hardcoded

- **Q: How to add a button click?**
  - A: Use ViewBinding: `binding.myButton.setOnClickListener(v -> { ... })`

## 📞 Project Status

**Current Phase:** Phase 1 Complete ✅
**Next Phase:** Phase 2 - Core CRUD Features
**Estimated Completion:** 20 weeks for full implementation

## 🎓 Key Concepts to Understand

Before continuing, make sure you understand:
- ✅ MVVM architecture (Model-View-ViewModel)
- ✅ Repository pattern (data source abstraction)
- ✅ Room database (ORM for SQLite)
- ✅ RxJava2 (reactive programming)
- ✅ ViewBinding (type-safe view access)
- ✅ Navigation Component (fragment navigation)

## 🚦 Ready to Start?

1. Open Android Studio
2. Load the project
3. Sync Gradle
4. Run the app to verify everything works
5. Start with ApiaryListFragment implementation
6. Follow the implementation plan in README.md

**Good luck with your beekeeping app! 🐝**

---

**Created:** February 13, 2025
**Framework Version:** Android API 26-34
**Language:** Java (100%)
**Architecture:** MVVM + Repository Pattern
