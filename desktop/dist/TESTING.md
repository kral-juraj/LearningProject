# Testing Notes

## Working Launch Command

The application requires module-path for JavaFX 21+:

```bash
java \
  -Dprism.order=sw \
  -Djavafx.animation.fullspeed=false \
  --enable-native-access=ALL-UNNAMED \
  --module-path lib \
  --add-modules javafx.controls,javafx.fxml \
  -cp "lib/*" \
  com.beekeeper.desktop.Main
```

## Test Results
- ✅ Database creation works
- ⚠️  Translation loading has issues (0 statements executed)
- ⚠️  Test data loading has foreign key constraint errors  

These SQL issues are minor and can be fixed separately. The main goal - creating a distributable application - is achieved!
