-- Hive extended details and activities translations
-- Generated: 2026-02-17
-- Categories: hive, label, tooltip, button

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES

-- Hive types
(lower(hex(randomblob(16))), 'hive.type.vertical', 'sk', 'Nádstavkový', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.type.vertical', 'en', 'Vertical (Langstroth)', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.type.horizontal', 'sk', 'Ležiak', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.type.horizontal', 'en', 'Horizontal', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.type.nuke', 'sk', 'Oddielka', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.type.nuke', 'en', 'Nucleus', 'hive', datetime('now')),

-- Frame types
(lower(hex(randomblob(16))), 'hive.frame.b', 'sk', 'Bratislavský (B)', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.b', 'en', 'Bratislava (B)', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.langstroth', 'sk', 'Langstroth', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.langstroth', 'en', 'Langstroth', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.dadant', 'sk', 'Dadant-Blatt', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.dadant', 'en', 'Dadant-Blatt', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.zander', 'sk', 'Zander', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.zander', 'en', 'Zander', 'hive', datetime('now')),

-- Activity types
(lower(hex(randomblob(16))), 'hive.activity.added_super', 'sk', 'Pridaný medeník', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.added_super', 'en', 'Added super', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.removed_super', 'sk', 'Odobraný medeník', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.removed_super', 'en', 'Removed super', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.added_queen_excluder', 'sk', 'Pridaná materská mriežka', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.added_queen_excluder', 'en', 'Added queen excluder', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.removed_queen_excluder', 'sk', 'Odobratá materská mriežka', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.removed_queen_excluder', 'en', 'Removed queen excluder', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.added_propolis_trap', 'sk', 'Pridaná propolisová mriežka', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.added_propolis_trap', 'en', 'Added propolis trap', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.removed_propolis_trap', 'sk', 'Odobratá propolisová mriežka', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.removed_propolis_trap', 'en', 'Removed propolis trap', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.added_insulation', 'sk', 'Pridané zateplenie', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.added_insulation', 'en', 'Added insulation', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.removed_insulation', 'sk', 'Odobrané zateplenie', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.removed_insulation', 'en', 'Removed insulation', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.changed_bottom_board', 'sk', 'Výmena dna', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.changed_bottom_board', 'en', 'Changed bottom board', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.honey_harvest', 'sk', 'Medobranie', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.honey_harvest', 'en', 'Honey harvest', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.treatment', 'sk', 'Liečenie', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.treatment', 'en', 'Treatment', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.swarmed', 'sk', 'Vyrojenie', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.swarmed', 'en', 'Swarmed', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.split_created', 'sk', 'Vytvorenie oddielky', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.split_created', 'en', 'Split created', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.requeened', 'sk', 'Výmena matky', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.requeened', 'en', 'Requeened', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.relocated', 'sk', 'Presun úľa', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.relocated', 'en', 'Relocated', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.repaired', 'sk', 'Oprava úľa', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.repaired', 'en', 'Repaired', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.combined', 'sk', 'Spojenie úľov', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.combined', 'en', 'Combined', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.other', 'sk', 'Iná aktivita', 'hive', datetime('now')),
(lower(hex(randomblob(16))), 'hive.activity.other', 'en', 'Other', 'hive', datetime('now')),

-- Labels for hive dialog
(lower(hex(randomblob(16))), 'label.frame_type', 'sk', 'Typ rámika', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.frame_type', 'en', 'Frame type', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.frame_count', 'sk', 'Počet rámikov', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.frame_count', 'en', 'Frame count', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.insulated', 'sk', 'Zateplený', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.insulated', 'en', 'Insulated', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.high_bottom_board', 'sk', 'Vysoké dno', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.high_bottom_board', 'en', 'High bottom board', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.low_bottom_board', 'sk', 'Nízke dno', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.low_bottom_board', 'en', 'Low bottom board', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_excluder', 'sk', 'Materská mriežka', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_excluder', 'en', 'Queen excluder', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.propolis_trap', 'sk', 'Propolisová mriežka', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.propolis_trap', 'en', 'Propolis trap', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.activity_history', 'sk', 'História aktivít', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.activity_history', 'en', 'Activity history', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.activity_type', 'sk', 'Typ aktivity', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.activity_type', 'en', 'Activity type', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.activity_date', 'sk', 'Dátum', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.activity_date', 'en', 'Date', 'label', datetime('now')),

-- Tooltips
(lower(hex(randomblob(16))), 'tooltip.frame_type', 'sk', 'Štandard použitých rámikov', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.frame_type', 'en', 'Standard of frames used', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.frame_count', 'sk', 'Celkový počet rámikov v úli', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.frame_count', 'en', 'Total number of frames in hive', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.insulated', 'sk', 'Úľ je zateplený na zimu', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.insulated', 'en', 'Hive is insulated for winter', 'tooltip', datetime('now')),

-- Buttons
(lower(hex(randomblob(16))), 'button.view_history', 'sk', 'Zobraziť históriu', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.view_history', 'en', 'View history', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.add_activity', 'sk', 'Pridať aktivitu', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.add_activity', 'en', 'Add activity', 'button', datetime('now')),

-- Dialog titles
(lower(hex(randomblob(16))), 'dialog.hive_details.title', 'sk', 'Detail úľa', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive_details.title', 'en', 'Hive details', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.add_activity.title', 'sk', 'Pridať aktivitu', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.add_activity.title', 'en', 'Add activity', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.edit_activity.title', 'sk', 'Upraviť aktivitu', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.edit_activity.title', 'en', 'Edit activity', 'dialog', datetime('now'));

-- Additional labels for HiveDialog
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
(lower(hex(randomblob(16))), 'label.basic_info', 'sk', 'Základné informácie', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.basic_info', 'en', 'Basic information', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.frame_info', 'sk', 'Informácie o rámikoch', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.frame_info', 'en', 'Frame information', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.equipment', 'sk', 'Vybavenie', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.equipment', 'en', 'Equipment', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_info', 'sk', 'Informácie o matke', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_info', 'en', 'Queen information', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_id', 'sk', 'ID matky', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_id', 'en', 'Queen ID', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_year', 'sk', 'Rok narodenia matky', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_year', 'en', 'Queen year', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_color', 'sk', 'Farba značenia', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_color', 'en', 'Queen color', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.bottom_board', 'sk', 'Typ dna', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.bottom_board', 'en', 'Bottom board type', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.select_frame_type', 'sk', 'Vyberte typ rámika', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.select_frame_type', 'en', 'Select frame type', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive.header_add', 'sk', 'Vytvorenie nového úľa', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive.header_add', 'en', 'Create new hive', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive.header_edit', 'sk', 'Úprava úľa', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive.header_edit', 'en', 'Edit hive', 'dialog', datetime('now'));

-- Additional label for HiveCard
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
(lower(hex(randomblob(16))), 'label.frames', 'sk', 'rámiky', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.frames', 'en', 'frames', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.inactive', 'sk', 'Neaktívny', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.inactive', 'en', 'Inactive', 'label', datetime('now'));
