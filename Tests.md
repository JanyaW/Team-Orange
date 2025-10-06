Division-by-Zero in charge per child calculation

Arrange:
Add records with 0 children and non-zero charges
Act:
Call method that computes charges per child
Assert:
Should be able to handle with no crash or divide-by-zero error.  Return 0 or Skip record


Regression Test with Perfect Linear Relationship
Arrange:
Create records where charges = 2 * BMI
Act:
Run regression and compute Pearson r
Assert:
r should be approximately 1.0

Case Sensitivity in Smoker Classification
Arrange:
Add records with "Smoker", "smoker", and "SMOKER"
Act:
Count smokers
Assert:
Normalize case(s) and count correctly

Adding Duplicate Records
Arrange:
Add identical Records objects to DataStorage.
Act:
Call size() and getRecords()
Assert:
Should reflect duplicates unless explicitly filtered

Test Sorting Regions with Tied Averages
Arrange:
Create regions with identical average charges.
Act: 
Sort regions.
Assert: 
Should maintain stable sort or document tie-breaking.
